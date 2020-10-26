package org.magmafoundation.magma.remapper.proxy;

import java.lang.reflect.Field;
import org.magmafoundation.magma.remapper.utils.RemappingUtils;

/**
 * ProxyField
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:48 am
 */
public class ProxyField {

    public static String getName(Field field) {
        return RemappingUtils.inverseMapFieldName(field.getDeclaringClass(), field.getName());
    }
}
