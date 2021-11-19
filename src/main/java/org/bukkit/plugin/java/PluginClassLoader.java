package org.bukkit.plugin.java;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.magmafoundation.magma.Magma;
import org.magmafoundation.magma.configuration.MagmaConfig;
import org.magmafoundation.magma.patcher.Patcher;
import org.magmafoundation.magma.remapper.ClassLoaderContext;
import org.magmafoundation.magma.remapper.mappingsModel.ClassMappings;
import org.magmafoundation.magma.remapper.utils.RemappingUtils;
import org.magmafoundation.magma.remapper.v2.ClassInheritanceProvider;
import org.magmafoundation.magma.remapper.v2.MagmaRemapper;
import org.magmafoundation.magma.remapper.v2.MappingLoader;
import org.magmafoundation.magma.remapper.v2.ReflectionMapping;
import org.magmafoundation.magma.remapper.v2.ReflectionTransformer;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public final class PluginClassLoader extends URLClassLoader {

    public JavaPlugin getPlugin() { return plugin; } // Spigot
    final JavaPlugin plugin;
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new java.util.concurrent.ConcurrentHashMap<String, Class<?>>(); // Spigot
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    private Patcher patcher;
    private java.util.logging.Logger logger; // Paper - add field

    // Magma Remapper v2
    private JarMapping jarMapping;
    private MagmaRemapper remapper;
    private LaunchClassLoader launchClassLoader;

    static {
        try {
            Class.forName("org.bukkit.FINDME");
            Class.forName("org.bukkit.craftbukkit.FINDME");
            Class.forName("org.spigotmc.FINDME");
            Class.forName("com.destroystokyo.paper.FINDME");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            }
        }

    PluginClassLoader(final JavaPluginLoader loader, final ClassLoader parent, final PluginDescriptionFile description, final File dataFolder, final File file) throws IOException, InvalidPluginException {
        super(new URL[] {file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();
        this.patcher = Magma.getInstance().getPatcherManager().getPatchByName(description.getName());

        this.launchClassLoader = parent instanceof LaunchClassLoader ? (LaunchClassLoader) parent : (LaunchClassLoader) MinecraftServer.getServerInstance().getClass().getClassLoader();;
        this.jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        this.jarMapping.setFallbackInheritanceProvider(provider);
        this.remapper = new MagmaRemapper(jarMapping);

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
        // Magma start - Forge can access Bukkit plugin classes (needs modified LaunchWrapper)
        // Inspired by https://github.com/terrainwax/ForgeCanCallBukkit
        if (MagmaConfig.instance.forgeBukkitAccess.getValues() && parent instanceof LaunchClassLoader) {
            try {
                Method method = parent.getClass().getDeclaredMethod("addChild", ClassLoader.class);
                method.invoke(parent, this);
            } catch(Exception ignored) {}
        }
        // Magma end
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if(MagmaConfig.instance.v2Remapper.getValues()){
            if (ReflectionMapping.isNMSPackage(name)) {
                String remappedClass = jarMapping.classes.getOrDefault(name.replace(".", "/"), name).replace("/", ".");
                return launchClassLoader.loadClass(remappedClass);
            }

            if (name.startsWith("org.bukkit.")) {
                throw new ClassNotFoundException(name);
            }

            Class<?> result = classes.get(name);
            synchronized (name.intern()) {
                if (result == null) {
                    if (checkGlobal) {
                        result = loader.getClassByName(name);
                    }

                    if (result == null) {
                        result = remappedFindClassV2(name);
                    }

                    if (result != null) {
                        loader.setClass(name, result);
                    }

                    if (result == null) {
                     try {
                            result = launchClassLoader.loadClass(name);
                           } catch (Throwable throwable) {
                            throw new ClassNotFoundException(name, throwable);
                        }
                    }

                    classes.put(name, result);
                }
            }

            return result;
        }else{
            ClassLoaderContext.put(this);
            Class<?> result;
            try {
                if (name.startsWith("net.minecraft.server." + Magma.getBukkitVersion())) {
                    ClassMappings remappedClass = RemappingUtils.jarMapping.byNMSName.get(name);
                    if (remappedClass == null) {
                        throw new ClassNotFoundException(name);
                    }
                    return Class.forName(remappedClass.getMcpName());
                }

                if (name.startsWith("org.bukkit.")) {
                    throw new ClassNotFoundException(name);
                }
                result = classes.get(name);
                synchronized (name.intern()) {
                    if (result == null) {
                        if (checkGlobal) {
                            result = loader.getClassByName(name);
                        }

                        if (result == null) {
                            result = remappedFindClass(name);

                            if (result != null) {
                                loader.setClass(name, result);
                            }
                        }

                        if (result == null) {
                            throw new ClassNotFoundException(name);
                        }

                        classes.put(name, result);
                    }
                }
            } finally {
                ClassLoaderContext.pop();
            }
            return result;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            jar.close();
        }
    }

    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }

    private Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;

        try {
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null) {
                InputStream stream = url.openStream();
                if (stream != null) {
                    byte[] bytecode = IOUtils.toByteArray(stream);

                    if (this.patcher != null) {
                        bytecode = this.patcher.transform(name.replace("/", "."), bytecode);
                    }

                    bytecode = RemappingUtils.remapFindClass(description, name, bytecode);
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    URL jarURL = jarURLConnection.getJarFileURL();
                    CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);

                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        this.resolveClass(result);
                    }
                }
            }
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }

        return result;
    }

    private Class<?> remappedFindClassV2(String name) throws ClassNotFoundException {
        Class<?> result = null;

        try {
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null) {
                InputStream stream = url.openStream();
                if (stream != null) {
                    byte[] bytecode = remapper.remapClassFile(stream, RuntimeRepo.getInstance());

                    // Magma start - Plugin Patcher
                    if (this.patcher != null) {
                        bytecode = this.patcher.transform(name.replace("/", "."), bytecode);
                    }
                    // Magma end
                    bytecode = ReflectionTransformer.transform(bytecode);

                    int dot = name.lastIndexOf('.');
                    if (dot != -1) {
                        String pkgName = name.substring(0, dot);
                        if (getPackage(pkgName) == null) {
                            try {
                                if (manifest != null) {
                                    definePackage(pkgName, manifest, url);
                                } else {
                                    definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                    }


                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    URL jarURL = jarURLConnection.getJarFileURL();
                    CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);

                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        this.resolveClass(result);
                    }
                }
            }
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }

        return result;
    }


    public PluginDescriptionFile getDescription() {
        return description;
    }
}
