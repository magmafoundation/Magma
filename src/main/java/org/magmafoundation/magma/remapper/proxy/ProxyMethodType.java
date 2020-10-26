package org.magmafoundation.magma.remapper.proxy;

import java.lang.invoke.MethodType;
import org.magmafoundation.magma.remapper.utils.RemappingUtils;

/**
 * ProxyMethodType
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:49 am
 */
public class ProxyMethodType {

    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader classLoader) throws IllegalArgumentException, TypeNotPresentException {
        return MethodType.fromMethodDescriptorString(RemappingUtils.remapMethodDesc(descriptor), classLoader);
    }
}

