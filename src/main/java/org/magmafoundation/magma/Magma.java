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

    private static final String NAME = "Magma";
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
    }

    public static Magma getInstance() {
        return INSTANCE;
    }

    public static String getName() {
        if(MagmaConfig.instance.overrideServerBrand.getValues()) return MagmaConfig.instance.serverBrand.getValues();
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
