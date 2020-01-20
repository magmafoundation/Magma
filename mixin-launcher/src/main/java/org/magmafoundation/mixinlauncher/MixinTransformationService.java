package org.magmafoundation.mixinlauncher;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;

import joptsimple.OptionSpecBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MixinTransformationService
 *
 * @author Redned
 * @since 19/01/2020 - 12:33 am
 */
public class MixinTransformationService implements ITransformationService {

    public static final Logger LOGGER = Logger.getLogger("Magma");
    public static final String NAME = "Magma";

    private final Set<ITransformationService> transformationServices = new HashSet<>();
    private final Map<String, ILaunchPluginService> launchPluginServices = launchPluginServices();

    public MixinTransformationService() {
        if (Launcher.INSTANCE == null) {
            throw new IllegalStateException("Launcher in ModLauncher has not been initialized!");
        }
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void arguments(BiFunction<String, String, OptionSpecBuilder> argumentBuilder) {
        this.transformationServices.forEach(transformationService -> transformationService.arguments(argumentBuilder));
    }

    @Override
    public void argumentValues(OptionResult option) {
        this.transformationServices.forEach(transformationService -> transformationService.argumentValues(option));
    }

    @Override
    public void initialize(IEnvironment environment) {
        this.transformationServices.forEach(transformationService -> transformationService.initialize(environment));
    }

    @Override
    public void beginScanning(IEnvironment environment) {
        this.transformationServices.forEach(transformationService -> transformationService.beginScanning(environment));
    }

    @Override
    public List<Map.Entry<String, Path>> runScan(IEnvironment environment) {
        List<Map.Entry<String, Path>> list = new ArrayList<>();
        this.transformationServices.forEach(transformationService -> {
            list.addAll(transformationService.runScan(environment));
        });

        return list;
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {
        if (this.launchPluginServices == null)
            throw new IncompatibleEnvironmentException("LaunchPluginServices is unavailable");

        if (Objects.equals(getClass().getClassLoader(), Launcher.class.getClassLoader()))
            return;

        try {
            URLClassLoader classLoader = (URLClassLoader) Launcher.class.getClassLoader();
            Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURLMethod.setAccessible(true);
            addURLMethod.invoke(classLoader, getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toURL());
        } catch (Throwable ex) {
            throw new IncompatibleEnvironmentException("Failed to invoke URLClassLoader::addURL");
        }

        registerLaunchPluginService("org.spongepowered.asm.launch.MixinLaunchPlugin");
        registerTransformationService("org.spongepowered.asm.launch.MixinTransformationService");
        registerLaunchPluginService("org.magmafoundation.mixinlauncher.MixinLaunchPluginService");

        for (ITransformationService transformationService : this.transformationServices) {
            transformationService.onLoad(env, otherServices);
        }
    }

    @Override
    public List<ITransformer> transformers() {
        List<ITransformer> list = new ArrayList<>();
        this.transformationServices.stream().map(ITransformationService::transformers).forEach(list::addAll);
        return list;
    }

    @SuppressWarnings("unchecked")
    private void registerTransformationService(String className) throws IncompatibleEnvironmentException {
        try {
            Class<? extends ITransformationService> transformationServiceClass = (Class<? extends ITransformationService>) Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            if (transformationServicePresent(transformationServiceClass)) {
                LOGGER.log(Level.WARNING, "{} is already registered", transformationServiceClass.getSimpleName());
                return;
            }

            ITransformationService transformationService = transformationServiceClass.newInstance();
            this.transformationServices.add(transformationService);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Encountered an error while registering " + className, ex);
            throw new IncompatibleEnvironmentException(String.format("Failed to register %s", className));
        }
    }

    @SuppressWarnings("unchecked")
    private void registerLaunchPluginService(String className) throws IncompatibleEnvironmentException {
        try {
            Class<? extends ILaunchPluginService> launchPluginServiceClass = (Class<? extends ILaunchPluginService>) Class.forName(className, true, Launcher.class.getClassLoader());
            if (launchPluginServicePresent(launchPluginServiceClass)) {
                LOGGER.log(Level.WARNING, "{} is already registered", launchPluginServiceClass.getSimpleName());
                return;
            }

            ILaunchPluginService launchPluginService = launchPluginServiceClass.newInstance();
            String pluginName = launchPluginService.name();
            this.launchPluginServices.put(pluginName, launchPluginService);

            List<Map<String, String>> mods = Launcher.INSTANCE.environment().getProperty(IEnvironment.Keys.MODLIST.get()).orElse(null);
            if (mods != null) {
                Map<String, String> mod = new HashMap<>();
                mod.put("name", pluginName);
                mod.put("type", "PLUGINSERVICE");
                String fileName = launchPluginServiceClass.getProtectionDomain().getCodeSource().getLocation().getFile();
                mod.put("file", fileName.substring(fileName.lastIndexOf('/')));
                mods.add(mod);
            }
        } catch (Throwable ex) {
            LOGGER.log(Level.WARNING, "Encountered an error while registering " + className, ex);
            throw new IncompatibleEnvironmentException(String.format("Failed to register %s", className));
        }
    }

    private boolean transformationServicePresent(Class<? extends ITransformationService> transformationServiceClass) {
        for (ITransformationService transformationService : this.transformationServices) {
            if (transformationServiceClass.isInstance(transformationService)) {
                return true;
            }
        }

        return false;
    }

    private boolean launchPluginServicePresent(Class<? extends ILaunchPluginService> launchPluginServiceClass) {
        for (ILaunchPluginService launchPluginService : Objects.requireNonNull(this.launchPluginServices).values()) {
            if (launchPluginServiceClass.isInstance(launchPluginService)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ILaunchPluginService> launchPluginServices() {
        try {
            Field pluginsField = LaunchPluginHandler.class.getDeclaredField("plugins");
            pluginsField.setAccessible(true);

            Field launchPluginsField = Launcher.class.getDeclaredField("launchPlugins");
            launchPluginsField.setAccessible(true);
            LaunchPluginHandler launchPluginHandler = (LaunchPluginHandler) launchPluginsField.get(Launcher.INSTANCE);
            return (Map<String, ILaunchPluginService>) pluginsField.get(launchPluginHandler);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Encountered an error while getting LaunchPluginServices", ex);
            return new HashMap<>();
        }
    }
}
