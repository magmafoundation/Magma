package org.magmafoundation.magma.commands.permission;

import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.IPermissionHandler;
import net.minecraftforge.server.permission.context.IContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

public class BukkitPermissionsHandler implements IPermissionHandler {

    private final Map<String, Permission> registeredNodes = new HashMap<>();

    @Override
    public void registerNode(String node, DefaultPermissionLevel level, String desc) {
        Permission permission = new Permission(node, desc, fromForge(level));
        DefaultPermissions.registerPermission(permission, false);
        registeredNodes.put(node, permission);
    }

    @Override
    public Collection<String> getRegisteredNodes() {
        return registeredNodes.keySet();
    }

    @Override
    public boolean hasPermission(GameProfile profile, String node, @Nullable IContext context) {
        Player player = Bukkit.getServer().getPlayer(profile.getId());
        return player != null && player.hasPermission(node);
    }

    @Override
    public String getNodeDescription(String node) {
        return registeredNodes.containsKey(node) ? registeredNodes.get(node).getDescription() : "No Description Set";
    }

    private PermissionDefault fromForge(DefaultPermissionLevel level) {
        switch (level) {
            case ALL:
                return PermissionDefault.TRUE;
            case OP:
                return PermissionDefault.OP;
            case NONE:
                return PermissionDefault.FALSE;
        }
        return PermissionDefault.FALSE;
    }
}
