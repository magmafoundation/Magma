package org.magmafoundation.magma;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.Bukkit;
import org.magmafoundation.magma.configuration.MagmaConfig;
import org.magmafoundation.magma.patcher.PatcherManager;

/**
 * Magma
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:01 am
 */
public class Magma {

    private static String NAME = "Magma";
    private static final String VERSION = (Magma.class.getPackage().getImplementationVersion() != null) ? Magma.class.getPackage().getImplementationVersion() : "dev-env";
    private static final String BUKKIT_VERSION = "v1_12_R1";
    private static final String NMS_PREFIX = "net/minecraft/server/";
    private static final String LIBRARY_VERSION = "3";
    private static Magma INSTANCE = new Magma();
    private PatcherManager patcherManager;

    public Magma() {
        INSTANCE = this;

        if (System.getProperty("log4j.configurationFile") == null) {
            System.setProperty("log4j.configurationFile", "log4j2_magma.xml");
        }

        try {

            setName(MagmaConfig.instance.overrideServerBrand.getValues() ? MagmaConfig.instance.serverBrandValue.getValues() : "Magma");

        } catch (Exception e){
            MinecraftServer.getServerInstance().logSevere("Error setting server brand, using default value 'Magma'");
        }

    }

    private static void setName(String name){
        Magma.NAME = name;
    }

    public static Magma getInstance() {
        return INSTANCE;
    }

    public static String getName() {
        return NAME;
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getBukkitVersion() {
        return BUKKIT_VERSION;
    }

    public static String getNmsPrefix() {
        return NMS_PREFIX;
    }

    public static String getLibraryVersion() {
        return LIBRARY_VERSION;
    }

    public PatcherManager getPatcherManager() {
        return patcherManager;
    }

    public void setPatcherManager(PatcherManager patcherManager) {
        this.patcherManager = patcherManager;
    }
}
