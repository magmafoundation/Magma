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

package org.magmafoundation.magma.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * PlayerAPI
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 04:57 pm
 */
public class PlayerAPI {

    public static Map<EntityPlayerMP, Integer> mods = new ConcurrentHashMap<>();
    public static Map<EntityPlayerMP, String> modList = new ConcurrentHashMap<>();

    /**
     * Gets the NMS Player.
     *
     * @param player - A Bukkit player.
     * @return NMS player.
     */
    public static EntityPlayerMP getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Gets the CraftBukkit Player.
     *
     * @param playerMP - NMS MpPlayer.
     * @return player - Bukkit Player
     */
    public static Player getCBPlayer(EntityPlayerMP playerMP) {
        return playerMP.getBukkitEntity().getPlayer();
    }

    /**
     * Check is the player has access to Op.
     *
     * @param entityPlayer - The player.
     * @return boolean - is op or not.
     */
    public static boolean isOp(EntityPlayer entityPlayer) {
        return ServerAPI.getNMSServer().getPlayerList().canSendCommands(entityPlayer.getGameProfile());
    }

    /**
     * Get player mod count.
     *
     * @param player - The player.
     * @return loaded mod count.
     */
    public static int getModSize(Player player) {
        return mods.get(getNMSPlayer(player)) == null ? 0 : mods.get(getNMSPlayer(player));
    }

    /**
     * Gets the list of loaded mods.
     *
     * @param player - The player
     * @return list of loaded mods.
     */
    public static String getModlist(Player player) {
        return modList.get(getNMSPlayer(player)) == null ? "null" : modList.get(getNMSPlayer(player));
    }

    /**
     * Checks if a mod is in the list.
     *
     * @param player - The player
     * @param modid for the mod wanted to check.
     * @return boolean - if it's in the list or not.
     */
    public static boolean hasMod(Player player, String modid) {
        return getModlist(player).contains(modid);
    }

}
