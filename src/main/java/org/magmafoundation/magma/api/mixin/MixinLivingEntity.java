package org.magmafoundation.magma.api.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.magmafoundation.magma.api.bridge.IBridgeLivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinLivingEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 04:57 pm
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntity extends MixinEntity implements IBridgeLivingEntity {

    @Mutable
    @Shadow
    @Final
    public int maxHurtResistantTime;

    @Shadow
    protected float lastDamage;

    @Shadow protected PlayerEntity attackingPlayer;

    @Override
    public int getMaxHurtResistantTime() {
        return maxHurtResistantTime;
    }

    @Override
    public void setMaxHurtResistantTime(int time) {
        maxHurtResistantTime = time;
    }

    @Override
    public double getLastDamage() {
        return lastDamage;
    }

    @Override
    public void setLastDamage(double damage) {
        lastDamage = (float) damage;
    }

    @Override
    public PlayerEntity getAttackingPlayer() {
        return attackingPlayer;
    }
}
