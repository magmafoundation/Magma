package org.bukkit.entity;

import static junit.framework.TestCase.assertNotNull;

import net.minecraft.entity.boss.dragon.phase.PhaseList;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderDragon;
import org.junit.Assert;
import org.junit.Test;

public class EnderDragonPhaseTest {

    @Test
    public void testNotNull() {
        for (EnderDragon.Phase phase : EnderDragon.Phase.values()) {
            PhaseList dragonControllerPhase = CraftEnderDragon.getMinecraftPhase(phase);
            assertNotNull(phase.name(), dragonControllerPhase);
            assertNotNull(phase.name(), CraftEnderDragon.getBukkitPhase(dragonControllerPhase));
        }
    }

    @Test
    public void testBukkitToMinecraft() {
        Assert.assertEquals("CIRCLING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CIRCLING), PhaseList.HOLDING_PATTERN); // PAIL: Rename HOLDING_PATTERN
        Assert.assertEquals("STRAFING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.STRAFING), PhaseList.STRAFE_PLAYER); // PAIL: Rename STRAFE_PLAYER
        Assert.assertEquals("FLY_TO_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.FLY_TO_PORTAL), PhaseList.LANDING_APPROACH); // PAIL: Rename LANDING_APPROACH
        Assert.assertEquals("LAND_ON_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LAND_ON_PORTAL), PhaseList.LANDING); // PAIL: Rename LANDING
        Assert.assertEquals("LEAVE_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LEAVE_PORTAL), PhaseList.TAKEOFF); // PAIL: Rename TAKEOFF
        Assert.assertEquals("BREATH_ATTACK", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.BREATH_ATTACK), PhaseList.SITTING_FLAMING); // PAIL: Rename SITTING_FLAMING
        Assert.assertEquals("SEARCH_FOR_BREATH_ATTACK_TARGET", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET),
            PhaseList.SITTING_SCANNING); // PAIL: Rename SITTING_SCANNING
        Assert.assertEquals("ROAR_BEFORE_ATTACK", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.ROAR_BEFORE_ATTACK), PhaseList.SITTING_ATTACKING); // PAIL: Rename SITTING_ATTACKING
        Assert.assertEquals("CHARGE_PLAYER", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CHARGE_PLAYER), PhaseList.CHARGING_PLAYER); // PAIL: Rename CHARGING_PLAYER
        Assert.assertEquals("DYING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.DYING), PhaseList.DYING); // PAIL: Rename DYING
        Assert.assertEquals("HOVER", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.HOVER), PhaseList.HOVER); // PAIL: Rename HOVER
    }

    @Test
    public void testMinecraftToBukkit() {
        Assert.assertEquals("CIRCLING", CraftEnderDragon.getBukkitPhase(PhaseList.HOLDING_PATTERN), EnderDragon.Phase.CIRCLING);
        Assert.assertEquals("STRAFING", CraftEnderDragon.getBukkitPhase(PhaseList.STRAFE_PLAYER), EnderDragon.Phase.STRAFING);
        Assert.assertEquals("FLY_TO_PORTAL", CraftEnderDragon.getBukkitPhase(PhaseList.LANDING_APPROACH), EnderDragon.Phase.FLY_TO_PORTAL);
        Assert.assertEquals("LAND_ON_PORTAL", CraftEnderDragon.getBukkitPhase(PhaseList.LANDING), EnderDragon.Phase.LAND_ON_PORTAL);
        Assert.assertEquals("LEAVE_PORTAL", CraftEnderDragon.getBukkitPhase(PhaseList.TAKEOFF), EnderDragon.Phase.LEAVE_PORTAL);
        Assert.assertEquals("BREATH_ATTACK", CraftEnderDragon.getBukkitPhase(PhaseList.SITTING_FLAMING), EnderDragon.Phase.BREATH_ATTACK);
        Assert.assertEquals("SEARCH_FOR_BREATH_ATTACK_TARGET", CraftEnderDragon.getBukkitPhase(PhaseList.SITTING_SCANNING), EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET);
        Assert.assertEquals("ROAR_BEFORE_ATTACK", CraftEnderDragon.getBukkitPhase(PhaseList.SITTING_ATTACKING), EnderDragon.Phase.ROAR_BEFORE_ATTACK);
        Assert.assertEquals("CHARGE_PLAYER", CraftEnderDragon.getBukkitPhase(PhaseList.CHARGING_PLAYER), EnderDragon.Phase.CHARGE_PLAYER);
        Assert.assertEquals("DYING", CraftEnderDragon.getBukkitPhase(PhaseList.DYING), EnderDragon.Phase.DYING);
        Assert.assertEquals("HOVER", CraftEnderDragon.getBukkitPhase(PhaseList.HOVER), EnderDragon.Phase.HOVER);
    }
}
