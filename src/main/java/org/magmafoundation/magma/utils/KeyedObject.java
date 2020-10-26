package org.magmafoundation.magma.utils;

import net.minecraft.util.ResourceLocation;

/**
 * KeyedObject
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/07/2020 - 05:04 pm
 */
public interface KeyedObject {

    ResourceLocation getMinecraftKey();

    default String getMinecraftKeyString() {
        return getMinecraftKey().toString();
    }
}
