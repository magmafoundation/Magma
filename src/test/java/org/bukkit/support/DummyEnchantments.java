package org.bukkit.support;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.potion.Potion;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionEffectType;
import org.bukkit.potion.PotionEffectType;

public class DummyEnchantments {

    static {
        Enchantments.SHARPNESS.getClass();
        for (Object enchantment : Enchantment.REGISTRY) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment((Enchantment) enchantment));
        }
        for (Object effect : Potion.REGISTRY) {
            PotionEffectType.registerPotionEffectType(new CraftPotionEffectType((Potion) effect));
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void setup() {
    }
}
