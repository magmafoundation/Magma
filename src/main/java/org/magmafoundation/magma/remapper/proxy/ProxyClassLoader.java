package org.magmafoundation.magma.remapper.proxy;

import org.magmafoundation.magma.remapper.utils.ASMUtils;
import org.magmafoundation.magma.remapper.utils.RemappingUtils;

/**
 * ProxyClassLoader
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:48 am
 */
public class ProxyClassLoader {

    public static Class<?> loadClass(final ClassLoader inst, String className) throws ClassNotFoundException {
        className = ASMUtils.toClassName(RemappingUtils.map(ASMUtils.toInternalName(className)));
        return inst.loadClass(className);
    }
}