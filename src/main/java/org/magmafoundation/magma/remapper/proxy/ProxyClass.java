package org.magmafoundation.magma.remapper.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import org.magmafoundation.magma.remapper.utils.ASMUtils;
import org.magmafoundation.magma.remapper.utils.RemappingUtils;

/**
 * ProxyClass
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/11/2019 - 08:47 am
 */
public class ProxyClass {

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, RemappingUtils.getCallerClassLoder());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(ASMUtils.toClassName(RemappingUtils.map(className.replace('.', '/'))), initialize, loader);
    }

    public static Method getDeclaredMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getDeclaredMethod, but class is null.methodname=" + name + ",parameters=" + Arrays.toString(parameterTypes));
        }
        return clazz.getDeclaredMethod(RemappingUtils.mapMethodName(clazz, name, parameterTypes), parameterTypes);
    }

    public static Method getMethod(Class clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getMethod, but class is null.methodname=" + name + ",parameters=" + Arrays.toString(parameterTypes));
        }
        return clazz.getMethod(RemappingUtils.mapMethodName(clazz, name, parameterTypes), parameterTypes);
    }

    public static Field getDeclaredField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getDeclaredField, but class is null.name=" + name);
        }
        return clazz.getDeclaredField(RemappingUtils.mapFieldName(clazz, name));
    }

    public static Field getField(Class clazz, String name) throws NoSuchFieldException, SecurityException {
        if (clazz == null) {
            throw new NullPointerException("call getField, but class is null.name=" + name);
        }
        return clazz.getField(RemappingUtils.mapFieldName(clazz, name));
    }

    public static String getName(Class clazz) {
        Objects.requireNonNull(clazz);
        return RemappingUtils.inverseMapName(clazz);
    }

    public static String getSimpleName(Class clazz) {
        Objects.requireNonNull(clazz);
        return RemappingUtils.inverseMapSimpleName(clazz);
    }
}
