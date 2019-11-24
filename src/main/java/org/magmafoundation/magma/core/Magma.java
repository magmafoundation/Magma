package org.magmafoundation.magma.core;

import java.util.logging.Logger;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("magma")
public class Magma {

    private static final Logger LOGGER = Logger.getLogger("Magma");
    private static final String NAME = "Magma";
    private static final String VERSION =
        (Magma.class.getPackage().getImplementationVersion() != null) ? Magma.class.getPackage()
            .getImplementationVersion() : "dev-env";
    private static final String BUKKIT_VERSION = "v1_14_4_R1";
    private static final String NMS_PREFIX = "net/minecraft/server/";

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

    public static Logger getLogger() {
        return LOGGER;
    }
}
