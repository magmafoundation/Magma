package org.bukkit.support;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Bootstrap;
import net.minecraft.potion.Potion;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionEffectType;
import org.bukkit.potion.PotionEffectType;
import org.junit.BeforeClass;

/**
 * If you are getting: java.lang.ExceptionInInitializerError at net.minecraft.server.StatisticList.<clinit>(SourceFile:58) at net.minecraft.server.Item.<clinit>(SourceFile:252) at
 * net.minecraft.server.Block.<clinit>(Block.java:577)
 * <p>
 * extend this class to solve it.
 */
public abstract class AbstractTestingBase {

    public static final List<Material> INVALIDATED_MATERIALS = ImmutableList.<Material>builder()
        .add(
            Material.BREWING_STAND,
            Material.BED_BLOCK,
            Material.NETHER_WARTS,
            Material.CAULDRON,
            Material.FLOWER_POT,
            Material.CROPS,
            Material.SUGAR_CANE_BLOCK,
            Material.CAKE_BLOCK,
            Material.SKULL,
            Material.PISTON_EXTENSION,
            Material.PISTON_MOVING_PIECE,
            Material.GLOWING_REDSTONE_ORE,
            Material.DIODE_BLOCK_ON,
            Material.PUMPKIN_STEM,
            Material.SIGN_POST,
            Material.REDSTONE_COMPARATOR_ON,
            Material.TRIPWIRE,
            Material.REDSTONE_LAMP_ON,
            Material.MELON_STEM,
            Material.REDSTONE_TORCH_OFF,
            Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_WIRE,
            Material.WALL_SIGN,
            Material.DIODE_BLOCK_OFF,
            Material.IRON_DOOR_BLOCK,
            Material.WOODEN_DOOR,
            Material.WATER,
            Material.STATIONARY_WATER,
            Material.LAVA,
            Material.STATIONARY_LAVA,
            Material.DOUBLE_STEP,
            Material.DOUBLE_STEP,
            Material.FIRE,
            Material.PORTAL,
            Material.ENDER_PORTAL,
            Material.WOOD_DOUBLE_STEP,
            Material.COCOA,
            Material.CARROT,
            Material.POTATO,
            Material.STANDING_BANNER,
            Material.WALL_BANNER,
            Material.DAYLIGHT_DETECTOR_INVERTED,
            Material.DOUBLE_STONE_SLAB2,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.PURPUR_DOUBLE_SLAB,
            Material.BEETROOT_BLOCK,
            Material.END_GATEWAY,
            Material.BURNING_FURNACE,
            Material.FROSTED_ICE
        ).build();

    @BeforeClass
    public static void setup() {
        Bootstrap.register();
        DummyServer.setup();
        DummyEnchantments.setup();
        // Magma add blocks Blocks
        for (Material material : Material.values()) {
            if (material.getId() < 256)
                Material.addBlockMaterial(material);
        }


    }
}
