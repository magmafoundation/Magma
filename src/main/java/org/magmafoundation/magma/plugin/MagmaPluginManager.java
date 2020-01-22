package org.magmafoundation.magma.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * MagmaPluginManager
 *
 * @author Redned
 * @since 21/10/2020 - 07:25 pm
 */
public class MagmaPluginManager implements PluginManager {

    @Override
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException {

    }

    @Nullable
    @Override
    public Plugin getPlugin(@NotNull String name) {
        return null;
    }

    @NotNull
    @Override
    public Plugin[] getPlugins() {
        return new Plugin[0];
    }

    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        return false;
    }

    @Override
    public boolean isPluginEnabled(@Nullable Plugin plugin) {
        return false;
    }

    @Nullable
    @Override
    public Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        return null;
    }

    @NotNull
    @Override
    public Plugin[] loadPlugins(@NotNull File directory) {
        return new Plugin[0];
    }

    @Override
    public void disablePlugins() {

    }

    @Override
    public void clearPlugins() {

    }

    @Override
    public void callEvent(@NotNull Event event) throws IllegalStateException {

    }

    @Override
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {

    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin) {

    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled) {

    }

    @Override
    public void enablePlugin(@NotNull Plugin plugin) {

    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {

    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin, boolean closeClassloader) {

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
        return false;
    }
}
