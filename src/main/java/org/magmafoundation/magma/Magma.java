package org.magmafoundation.magma;

import org.bukkit.Bukkit;
import org.magmafoundation.magma.configuration.MagmaConfig;
import org.magmafoundation.magma.remapper.remappers.MagmaClassRepo;
import org.magmafoundation.magma.remapper.utils.RemappingUtils;

/**
 * Magma
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:01 am
 */
public class Magma {

    private static Magma INSTANCE;

    private MagmaConfig magmaConfig;
    private MagmaClassRepo magmaClassRepo;

    private static final String NAME = "Magma";
    private static final String VERSION = (Magma.class.getPackage().getImplementationVersion() != null) ? Magma.class.getPackage().getImplementationVersion() : "dev-env";
    private static String BUKKIT_VERSION;
    private static final String NMS_PREFIX = "net/minecraft/server/";
    private static final String LIBRARIES_VERSION = "1";

    public Magma() {
        // Create default Magma instance.
        INSTANCE = this;

        BUKKIT_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);


        this.magmaConfig = new MagmaConfig();
        this.magmaClassRepo = new MagmaClassRepo();
    }

    /**
     *  Get the name of our server software
     *
     * @return the name
     */
    public static String getName() {
        return NAME;
    }

    /**
     * Get the current Magma version
     *
     * @return Current version
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * Get current Bukkit version.
     *
     * @return Current version
     */
    public static String getBukkitVersion() {
        return BUKKIT_VERSION;
    }

    /**
     * Get Net Minecraft Server prefix
     *
     * @return NMS prefix
     */
    public static String getNmsPrefix() {
        return NMS_PREFIX;
    }

    /**
     * Get Bukkit/Forge required libraries version
     *
     * @return library version
     */
    public static String getLibrariesVersion() {
        return LIBRARIES_VERSION;
    }

    /**
     * Get the Magma config
     *
     * @return Magma config
     */
    public MagmaConfig getMagmaConfig() {
        return magmaConfig;
    }

    /**
     * Not sure what this does
     *
     * @return
     */
    public MagmaClassRepo getMagmaClassRepo() {
        return magmaClassRepo;
    }

    /**
     * Get the instance of this class
     *
     * @return class instance
     */
    public static Magma getInstance() {
        return INSTANCE;
    }
}
