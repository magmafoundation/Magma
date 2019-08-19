package org.magmafoundation.magma;

/**
 * Magma
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:01 am
 */
public class Magma {

    private static final String NAME = "Magma";
    private static final String VERSION = (Magma.class.getPackage().getImplementationVersion() != null) ? Magma.class.getPackage().getImplementationVersion() : "dev-env";

    public static String getName() {
        return NAME;
    }

    public static String getVersion() {
        return VERSION;
    }
}
