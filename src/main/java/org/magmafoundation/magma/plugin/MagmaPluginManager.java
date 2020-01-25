package org.magmafoundation.magma.plugin;

import co.aikar.timings.Timings;
import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerEventException;
import com.destroystokyo.paper.exception.ServerPluginEnableDisableException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.bukkit.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MagmaPluginManager
 *
 * @author Redned
 * @since 21/10/2020 - 07:25 pm
 */
public class MagmaPluginManager implements PluginManager {

    private Server server;

    private final Map<Pattern, PluginLoader> fileAssociations = new HashMap<Pattern, PluginLoader>();
    private final List<Plugin> plugins = new ArrayList<Plugin>();
    private final Map<String, Plugin> lookupNames = new HashMap<String, Plugin>();
    private File updateDirectory;
    private final SimpleCommandMap commandMap;
    private final Map<String, Permission> permissions = new HashMap<String, Permission>();
    private final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<Boolean, Set<Permission>>();
    private final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<String, Map<Permissible, Boolean>>();
    private final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<Boolean, Map<Permissible, Boolean>>();
    private boolean useTimings = false;

    public MagmaPluginManager(Server server, SimpleCommandMap commandMap) {
        this.server = server;
        this.commandMap = commandMap;
    }

    @Override
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        PluginLoader instance;

        if (PluginLoader.class.isAssignableFrom(loader)) {
            Constructor<? extends PluginLoader> constructor;

            try {
                constructor = loader.getConstructor(Server.class);
                instance = constructor.newInstance(server);
            } catch (NoSuchMethodException ex) {
                String className = loader.getName();

                throw new IllegalArgumentException(String.format("Class %s does not have a public %s(Server) constructor", className, className), ex);
            } catch (Exception ex) {
                throw new IllegalArgumentException(String.format("Unexpected exception %s while attempting to construct a new instance of %s", ex.getClass().getName(), loader.getName()), ex);
            }
        } else {
            throw new IllegalArgumentException(String.format("Class %s does not implement interface PluginLoader", loader.getName()));
        }

        Pattern[] patterns = instance.getPluginFileFilters();

        synchronized (this) {
            Arrays.stream(patterns).forEach(pattern -> fileAssociations.put(pattern, instance));
        }
    }

    @Nullable
    @Override
    public Plugin getPlugin(@NotNull String name) {
        return lookupNames.get(name.replace(' ', '_').toLowerCase(java.util.Locale.ENGLISH));
    }

    @NotNull
    @Override
    public Plugin[] getPlugins() {
        return plugins.toArray(new Plugin[plugins.size()]);
    }

    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        Plugin plugin = getPlugin(name);

        return isPluginEnabled(plugin);
    }

    @Override
    public boolean isPluginEnabled(@Nullable Plugin plugin) {
        if ((plugin != null) && (plugins.contains(plugin))) {
            return plugin.isEnabled();
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        Validate.notNull(file, "File cannot be null");

        checkUpdate(file);

        Set<Pattern> filters = fileAssociations.keySet();
        Plugin result = null;

        for (Pattern filter : filters) {
            String name = file.getName();
            Matcher match = filter.matcher(name);

            if (match.find()) {
                PluginLoader loader = fileAssociations.get(filter);

                result = loader.loadPlugin(file);
            }
        }

        if (result != null) {
            plugins.add(result);
            lookupNames.put(result.getDescription().getName().toLowerCase(java.util.Locale.ENGLISH), result); // Paper
        }

        return result;
    }

    private void checkUpdate(@NotNull File file) {
        if (updateDirectory == null || !updateDirectory.isDirectory()) {
            return;
        }

        File updateFile = new File(updateDirectory, file.getName());
        if (updateFile.isFile() && FileUtil.copy(updateFile, file)) {
            updateFile.delete();
        }
    }


    @NotNull
    @Override
    public Plugin[] loadPlugins(@NotNull File directory) {
        Validate.notNull(directory, "Directory cannot be null");
        Validate.isTrue(directory.isDirectory(), "Directory must be a directory");

        List<Plugin> result = new ArrayList<Plugin>();
        Set<Pattern> filters = fileAssociations.keySet();

        if (!(server.getUpdateFolder().equals(""))) {
            updateDirectory = new File(directory, server.getUpdateFolder());
        }

        Map<String, File> plugins = new HashMap<String, File>();
        Set<String> loadedPlugins = new HashSet<String>();
        Map<String, Collection<String>> dependencies = new HashMap<String, Collection<String>>();
        Map<String, Collection<String>> softDependencies = new HashMap<String, Collection<String>>();

        // This is where it figures out all possible plugins
        for (File file : directory.listFiles()) {
            PluginLoader loader = null;
            for (Pattern filter : filters) {
                Matcher match = filter.matcher(file.getName());
                if (match.find()) {
                    loader = fileAssociations.get(filter);
                }
            }

            if (loader == null) {
                continue;
            }

            PluginDescriptionFile description = null;
            try {
                description = loader.getPluginDescription(file);
                String name = description.getName();
                if (name.equalsIgnoreCase("bukkit") || name.equalsIgnoreCase("minecraft") || name.equalsIgnoreCase("mojang")) {
                    server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': Restricted Name");
                    continue;
                } else if (description.getRawName().indexOf(' ') != -1) {
                    server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': uses the space-character (0x20) in its name");
                    continue;
                }
            } catch (InvalidDescriptionException ex) {
                server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", ex);
                continue;
            }

            File replacedFile = plugins.put(description.getName(), file);
            if (replacedFile != null) {
                server.getLogger().severe(String.format(
                    "Ambiguous plugin name `%s' for files `%s' and `%s' in `%s'",
                    description.getName(),
                    file.getPath(),
                    replacedFile.getPath(),
                    directory.getPath()
                ));
            }

            Collection<String> softDependencySet = description.getSoftDepend();
            if (softDependencySet != null && !softDependencySet.isEmpty()) {
                if (softDependencies.containsKey(description.getName())) {
                    // Duplicates do not matter, they will be removed together if applicable
                    softDependencies.get(description.getName()).addAll(softDependencySet);
                } else {
                    softDependencies.put(description.getName(), new LinkedList<String>(softDependencySet));
                }
            }

            Collection<String> dependencySet = description.getDepend();
            if (dependencySet != null && !dependencySet.isEmpty()) {
                dependencies.put(description.getName(), new LinkedList<String>(dependencySet));
            }

            Collection<String> loadBeforeSet = description.getLoadBefore();
            if (loadBeforeSet != null && !loadBeforeSet.isEmpty()) {
                for (String loadBeforeTarget : loadBeforeSet) {
                    if (softDependencies.containsKey(loadBeforeTarget)) {
                        softDependencies.get(loadBeforeTarget).add(description.getName());
                    } else {
                        // softDependencies is never iterated, so 'ghost' plugins aren't an issue
                        Collection<String> shortSoftDependency = new LinkedList<String>();
                        shortSoftDependency.add(description.getName());
                        softDependencies.put(loadBeforeTarget, shortSoftDependency);
                    }
                }
            }
        }

        while (!plugins.isEmpty()) {
            boolean missingDependency = true;
            Iterator<Map.Entry<String, File>> pluginIterator = plugins.entrySet().iterator();

            while (pluginIterator.hasNext()) {
                Map.Entry<String, File> entry = pluginIterator.next();
                String plugin = entry.getKey();

                if (dependencies.containsKey(plugin)) {
                    Iterator<String> dependencyIterator = dependencies.get(plugin).iterator();

                    while (dependencyIterator.hasNext()) {
                        String dependency = dependencyIterator.next();

                        // Dependency loaded
                        if (loadedPlugins.contains(dependency)) {
                            dependencyIterator.remove();

                            // We have a dependency not found
                        } else if (!plugins.containsKey(dependency)) {
                            missingDependency = false;
                            pluginIterator.remove();
                            softDependencies.remove(plugin);
                            dependencies.remove(plugin);

                            server.getLogger().log(
                                Level.SEVERE,
                                "Could not load '" + entry.getValue().getPath() + "' in folder '" + directory.getPath() + "'",
                                new UnknownDependencyException(dependency));
                            break;
                        }
                    }

                    if (dependencies.containsKey(plugin) && dependencies.get(plugin).isEmpty()) {
                        dependencies.remove(plugin);
                    }
                }
                if (softDependencies.containsKey(plugin)) {
                    Iterator<String> softDependencyIterator = softDependencies.get(plugin).iterator();

                    while (softDependencyIterator.hasNext()) {
                        String softDependency = softDependencyIterator.next();

                        // Soft depend is no longer around
                        if (!plugins.containsKey(softDependency)) {
                            softDependencyIterator.remove();
                        }
                    }

                    if (softDependencies.get(plugin).isEmpty()) {
                        softDependencies.remove(plugin);
                    }
                }
                if (!(dependencies.containsKey(plugin) || softDependencies.containsKey(plugin)) && plugins.containsKey(plugin)) {
                    // We're clear to load, no more soft or hard dependencies left
                    File file = plugins.get(plugin);
                    pluginIterator.remove();
                    missingDependency = false;

                    try {
                        result.add(loadPlugin(file));
                        loadedPlugins.add(plugin);
                        continue;
                    } catch (InvalidPluginException | InvalidDescriptionException ex) {
                        server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", ex);
                    }
                }
            }

            if (missingDependency) {
                // We now iterate over plugins until something loads
                // This loop will ignore soft dependencies
                pluginIterator = plugins.entrySet().iterator();

                while (pluginIterator.hasNext()) {
                    Map.Entry<String, File> entry = pluginIterator.next();
                    String plugin = entry.getKey();

                    if (!dependencies.containsKey(plugin)) {
                        softDependencies.remove(plugin);
                        missingDependency = false;
                        File file = entry.getValue();
                        pluginIterator.remove();

                        try {
                            result.add(loadPlugin(file));
                            loadedPlugins.add(plugin);
                            break;
                        } catch (InvalidPluginException | InvalidDescriptionException ex) {
                            server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", ex);
                        }
                    }
                }
                // We have no plugins left without a depend
                if (missingDependency) {
                    softDependencies.clear();
                    dependencies.clear();
                    Iterator<File> failedPluginIterator = plugins.values().iterator();

                    while (failedPluginIterator.hasNext()) {
                        File file = failedPluginIterator.next();
                        failedPluginIterator.remove();
                        server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': circular dependency detected");
                    }
                }
            }
        }

        return result.toArray(new Plugin[result.size()]);
    }

    @Override
    public void disablePlugins() {
        disablePlugins(false);
    }

    public void disablePlugins(boolean closeClassloaders) {
        Plugin[] plugins = getPlugins();
        for (int i = plugins.length - 1; i >= 0; i--) {
            disablePlugin(plugins[i], closeClassloaders); // Paper - close Classloader on disable
        }
    }

    @Override
    public void clearPlugins() {
        synchronized (this) {
            disablePlugins(true); // Paper - close Classloader on disable
            plugins.clear();
            lookupNames.clear();
            HandlerList.unregisterAll();
            fileAssociations.clear();
            permissions.clear();
            defaultPerms.get(true).clear();
            defaultPerms.get(false).clear();
        }
    }

    @Override
    public void callEvent(@NotNull Event event) throws IllegalStateException {
        // Paper - replace callEvent by merging to below method
        if (event.isAsynchronous() && server.isPrimaryThread()) {
            throw new IllegalStateException(event.getEventName() + " may only be triggered asynchronously.");
        } else if (!event.isAsynchronous() && !server.isPrimaryThread()) {
            throw new IllegalStateException(event.getEventName() + " may only be triggered synchronously.");
        }

        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();

        for (RegisteredListener registration : listeners) {
            if (!registration.getPlugin().isEnabled()) {
                continue;
            }

            try {
                registration.callEvent(event);
            } catch (AuthorNagException ex) {
                Plugin plugin = registration.getPlugin();

                if (plugin.isNaggable()) {
                    plugin.setNaggable(false);

                    server.getLogger().log(Level.SEVERE, String.format(
                        "Nag author(s): '%s' of '%s' about the following: %s",
                        plugin.getDescription().getAuthors(),
                        plugin.getDescription().getFullName(),
                        ex.getMessage()
                    ));
                }
            } catch (Throwable ex) {
                // Paper start - error reporting
                String msg = "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getFullName();
                server.getLogger().log(Level.SEVERE, msg, ex);
                if (!(event instanceof ServerExceptionEvent)) { // We don't want to cause an endless event loop
                    callEvent(new ServerExceptionEvent(new ServerEventException(msg, ex, registration.getPlugin(), registration.getListener(), event)));
                }
                // Paper end
            }
        }
    }

    @Override
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
        }

        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
        }
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin) {
        registerEvent(event, listener, priority, executor, plugin, false);
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled) {
        Validate.notNull(listener, "Listener cannot be null");
        Validate.notNull(priority, "Priority cannot be null");
        Validate.notNull(executor, "Executor cannot be null");
        Validate.notNull(plugin, "Plugin cannot be null");

        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");
        }

        executor = new co.aikar.timings.TimedEventExecutor(executor, plugin, null, event); // Paper
        if (false) { // Spigot - RL handles useTimings check now // Paper
            getEventListeners(event).register(new TimedRegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
        } else {
            getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
        }
    }

    @NotNull
    private HandlerList getEventListeners(@NotNull Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    @NotNull
    private Class<? extends Event> getRegistrationClass(@NotNull Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                && !clazz.getSuperclass().equals(Event.class)
                && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }


    @Override
    public void enablePlugin(@NotNull Plugin plugin) {
        if (!plugin.isEnabled()) {
            List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);

            if (!pluginCommands.isEmpty()) {
                commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
            }

            try {
                plugin.getPluginLoader().enablePlugin(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while enabling "
                    + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin);
            }

            HandlerList.bakeAll();
        }
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        disablePlugin(plugin, false);
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin, boolean closeClassloader) {
// Paper end - close Classloader on disable
        if (plugin.isEnabled()) {
            try {
                plugin.getPluginLoader().disablePlugin(plugin, closeClassloader); // Paper - close Classloader on disable
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while disabling "
                    + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                server.getScheduler().cancelTasks(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while cancelling tasks for "
                    + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                server.getServicesManager().unregisterAll(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while unregistering services for "
                    + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                HandlerList.unregisterAll(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while unregistering events for "
                    + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                server.getMessenger().unregisterIncomingPluginChannel(plugin);
                server.getMessenger().unregisterOutgoingPluginChannel(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while unregistering plugin channels for "
                    + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                for (World world : server.getWorlds()) {
                    world.removePluginChunkTickets(plugin);
                }
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while removing chunk tickets for " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
            }
        }
    }

    private void handlePluginException(String msg, Throwable ex, Plugin plugin) {
        server.getLogger().log(Level.SEVERE, msg, ex);
        callEvent(new ServerExceptionEvent(new ServerPluginEnableDisableException(msg, ex, plugin)));
    }


    @Nullable
    @Override
    public Permission getPermission(@NotNull String name) {
        return null;
    }

    @Override
    public void addPermission(@NotNull Permission perm) {

    }

    @Override
    public void removePermission(@NotNull Permission perm) {

    }

    @Override
    public void removePermission(@NotNull String name) {

    }

    @NotNull
    @Override
    public Set<Permission> getDefaultPermissions(boolean op) {
        return new HashSet<>();
    }

    @Override
    public void recalculatePermissionDefaults(@NotNull Permission perm) {

    }

    @Override
    public void subscribeToPermission(@NotNull String permission, @NotNull Permissible permissible) {

    }

    @Override
    public void unsubscribeFromPermission(@NotNull String permission, @NotNull Permissible permissible) {

    }

    @NotNull
    @Override
    public Set<Permissible> getPermissionSubscriptions(@NotNull String permission) {
        return new HashSet<>();
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, @NotNull Permissible permissible) {

    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, @NotNull Permissible permissible) {

    }

    @NotNull
    @Override
    public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        return new HashSet<>();
    }

    @NotNull
    @Override
    public Set<Permission> getPermissions() {
        return new HashSet<>();
    }

    @Override
    public boolean useTimings() {
        return Timings.isTimingsEnabled();
    }

    public void useTimings(boolean use) {
        Timings.setTimingsEnabled(use);
    }
}
