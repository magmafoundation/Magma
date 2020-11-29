package org.bukkit;

import net.minecraft.util.EnumParticleTypes;
import org.bukkit.craftbukkit.v1_12_R1.CraftParticle;
import org.junit.Assert;
import org.junit.Test;

public class ParticleTest {

    @Test
    public void verifyMapping() {
        for (Particle bukkit : Particle.values()) {
            Assert.assertNotNull("Missing Bukkit->NMS particle mapping", CraftParticle.toNMS(bukkit));
        }
        for (EnumParticleTypes nms : EnumParticleTypes.values()) {
            Assert.assertNotNull("Missing NMS->Bukkit particle mapping", CraftParticle.toBukkit(nms));
        }
    }
}
