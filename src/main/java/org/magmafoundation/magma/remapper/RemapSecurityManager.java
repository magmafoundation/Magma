package org.magmafoundation.magma.remapper;

/**
 * RemapSecurityManager
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 07:54 am
 */
public class RemapSecurityManager extends SecurityManager {

    public Class<?> getCallerClass(final int skip) {
        return (Class<?>) this.getClassContext()[skip + 1];
    }
}