package org.magmafoundation.magma.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * @param modid  for the mod wanted to check.
     * @return boolean - if it's in the list or not.
     */
    public static boolean hasMod(Player player, String modid) {
        return getModlist(player).contains(modid);
    }

}
