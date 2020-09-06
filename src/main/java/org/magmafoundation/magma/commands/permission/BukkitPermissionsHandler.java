/*
 * Magma Server
 * Copyright (C) 2019-2020.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
