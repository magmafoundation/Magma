package org.magmafoundation.magma.patcher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Patcher
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 05/03/2020 - 09:14 pm
 */
public abstract class Patcher {

    private String name = getClass().getAnnotation(PatcherInfo.class).name();
    private String description = getClass().getAnnotation(PatcherInfo.class).description();

    public abstract byte[] transform(String className, byte[] clazz);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PatcherInfo {

        String name();

        String description();
    }


}
