package org.magmafoundation.magma.api.core.util;


import net.minecraft.util.ResourceLocation;
import org.bukkit.NamespacedKey;

public class MagmaNamespacedLocation {


    public static NamespacedKey fromString(String string) {
        return fromMinecraft(new ResourceLocation(string));
    }

    public static NamespacedKey fromMinecraft(ResourceLocation minecraft) {
        return new NamespacedKey(minecraft.getNamespace(), minecraft.getPath());
    }

    public static ResourceLocation toMinecraft(NamespacedKey key) {
        return new ResourceLocation(key.getNamespace(), key.getKey());
    }
}
