package org.magmafoundation.magma.api;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerAPI
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 04:47 pm
 */
public class ServerAPI {

    public static Map<String, Integer> mods = new ConcurrentHashMap<>();
    public static Set<String> modList = new ConcurrentSet<>();

    /**
     * How many mods are loaded.
     *
     * @return int - loaded mods.
     */
    public static int getModSize() {
        return mods.get("mods") == null ? 0 : mods.get("mods");
    }

    /**
     * List all loaded mods by name.
     *
     * @return String - List of mods.
     */
    public static String getModList() {
        return modList.toString();
    }

    /**
     * Checks if a mod is in the list.
     *
     * @param modid for the mod to check.
     * @return boolean - if it's in the list or not.
     */
    public static boolean hasMod(String modid) {
        return getModList().contains(modid);
    }

    /**
     * Gets the Minecraft Server instance.
     *
     * @return MinecraftServer instance.
     */
    public static MinecraftServer getNMSServer() {
        return MinecraftServer.getServerInstance();
    }

    /**
     * Gets the CraftBukkit Server
     *
     * @return CraftServer instance.
     */
    public static CraftServer getCBServer() {
        return (CraftServer) Bukkit.getServer();
    }

}
