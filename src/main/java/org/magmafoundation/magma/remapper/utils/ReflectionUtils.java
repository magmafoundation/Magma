package org.magmafoundation.magma.remapper.utils;

/**
 * ReflectionUtils
 *
 * Move into its own class due to not loading correctly with plugins
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/06/2020 - 11:10 pm
 */
public class ReflectionUtils {

    private static final SecurityManager securityManager = new SecurityManager();

    public static Class<?> getCallerClass(int skip) {
        return securityManager.getCallerClass(skip);
    }

    public static ClassLoader getCallerClassLoader() {
        return ReflectionUtils.getCallerClass(3).getClassLoader();
    }

    static class SecurityManager extends java.lang.SecurityManager {

        public Class<?> getCallerClass(int skip) {
            return getClassContext()[skip + 1];
        }
    }
}
