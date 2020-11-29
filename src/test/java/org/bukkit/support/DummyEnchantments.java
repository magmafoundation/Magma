package org.bukkit.support;

import net.minecraft.init.Enchantments;

public class DummyEnchantments {

    static {
        Enchantments.SHARPNESS.getClass();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void setup() {
    }
}
