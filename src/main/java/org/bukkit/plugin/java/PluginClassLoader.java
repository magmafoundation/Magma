package org.bukkit.plugin.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import io.netty.util.internal.ConcurrentSet;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.magmafoundation.magma.Magma;
import org.magmafoundation.magma.patcher.Patcher;
import org.magmafoundation.magma.remapper.v2.*;
import org.magmafoundation.magma.remapper.v2.util.PackageUtil;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public final class PluginClassLoader extends URLClassLoader {

    public JavaPlugin getPlugin() { return plugin; } // Spigot
    final JavaPlugin plugin;
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;

    private Patcher patcher; // Magma - Plugin Patcher

    // Magma Remapper v2
    private JarMapping jarMapping;
    private MagmaRemapper remapper;
    private LaunchClassLoader launchClassLoader;

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

        this.launchClassLoader = parent instanceof LaunchClassLoader ? (LaunchClassLoader) parent : (LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader();
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
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (ReflectionMapping.isNMSPackage(name)) {
            String remappedClass = jarMapping.classes.getOrDefault(name.replace(".", "/"), name);
            return launchClassLoader.findClass(remappedClass);
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
                    result = remappedFindClass(name);

                    if (result != null) {
                        loader.setClass(name, result);
                    }
                }

                if (result == null) {
                    if (checkGlobal) {
                        try {
                            result = launchClassLoader.getClass().getClassLoader().loadClass(name);
                        } catch (Throwable throwable) {
                            throw new ClassNotFoundException(name, throwable);
                        }
                    }
                }

                if (result == null) {
                    throw new ClassNotFoundException(name);
                }

                classes.put(name, result);
            }
        }

        return result;
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
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    URL jarURL = jarURLConnection.getJarFileURL();

                    // Remap the classes
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
                        Package pkg = getPackage(pkgName);
                        if (pkg == null) {
                            try {
                                if (manifest != null) {
                                    pkg = definePackage(pkgName, manifest, url);
                                } else {
                                    pkg = definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (Exception ignored) {
                            }
                        }
                        if (pkg != null && manifest != null) {
                            PackageUtil.getInstance().fixPackage(pkg, manifest);
                        }
                    }

                    // Define the classes
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

    protected Package getPackage(String name) {
        if ("org.bukkit.craftbukkit".equals(name))
            name = "org.bukkit.craftbukkit." + Magma.getBukkitVersion();
        return super.getPackage(name);
    }

    public PluginDescriptionFile getDescription() {
        return description;
    }
}
