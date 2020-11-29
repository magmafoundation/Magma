package org.bukkit;

import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class BiomeTest extends AbstractTestingBase {

    @Test
    public void testBukkitToMinecraft() {
        for (Biome biome : Biome.values()) {
            Assert.assertNotNull("No NMS mapping for " + biome, CraftBlock.biomeToBiomeBase(biome));
        }
    }

    @Test
    public void testMinecraftToBukkit() {
        for (Object biome : net.minecraft.world.biome.Biome.REGISTRY) {
            Assert.assertNotNull("No Bukkit mapping for " + biome, CraftBlock.biomeBaseToBiome((net.minecraft.world.biome.Biome) biome));
        }
    }
}
