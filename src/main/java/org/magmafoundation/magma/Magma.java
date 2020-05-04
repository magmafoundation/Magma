package org.magmafoundation.magma;

import org.magmafoundation.magma.configuration.MagmaConfig;
import org.magmafoundation.magma.patcher.PatcherManager;

/**
 * Magma
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:01 am
 */
public class Magma {

    private static Magma INSTANCE;

    private MagmaConfig magmaConfig;

    private PatcherManager patcherManager;

    private static final String NAME = "Magma";
    private static final String VERSION = (Magma.class.getPackage().getImplementationVersion() != null) ? Magma.class.getPackage().getImplementationVersion() : "dev-env";
    private static final String BUKKIT_VERSION = "v1_12_R1";
    private static final String NMS_PREFIX = "net/minecraft/server/";
    private static final String LIBRARY_VERSION = "3";

    public Magma() {
        INSTANCE = this;

        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "log4j2_magma.xml");

        patcherManager = new PatcherManager();
        patcherManager.init();

        this.magmaConfig = new MagmaConfig();
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

    public MagmaConfig getMagmaConfig() {
        return magmaConfig;
    }

    public PatcherManager getPatcherManager() {
        return patcherManager;
    }
}
