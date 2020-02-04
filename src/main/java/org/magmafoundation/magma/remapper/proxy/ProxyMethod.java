package org.magmafoundation.magma.remapper.proxy;

import org.magmafoundation.magma.remapper.utils.RemappingUtils;

import java.lang.reflect.Method;

/**
 * ProxyMethod
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:49 am
 */
public class ProxyMethod {

    public static String getName(Method method) {
        return RemappingUtils.inverseMapMethodName(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
    }
}