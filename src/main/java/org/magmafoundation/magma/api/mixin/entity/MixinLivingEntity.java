package org.magmafoundation.magma.api.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import org.magmafoundation.magma.api.bridge.entity.IBridgeLivingEntity;
import org.magmafoundation.magma.api.core.entity.MagmaLivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * MixinLivingEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 04:57 pm
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity implements IBridgeLivingEntity {

    @Mutable
    @Shadow
    @Final
    public int maxHurtResistantTime;

    @Shadow
    protected float lastDamage;

    @Shadow protected PlayerEntity attackingPlayer;

    @Shadow
    protected boolean dead;

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract boolean isOnLadder();

    public boolean canPickUpLoot;
    public boolean collides = true;

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

    @Override
    public boolean getCanPickUpLoot() {
        return canPickUpLoot;
    }

    @Override
    public void setCanPickUpLoot(boolean loot) {
        canPickUpLoot = loot;
    }

    @Redirect(method = "canBeCollidedWith", at = @At("HEAD"))
    public boolean canBeCollidedWith() {
        return !dead && collides;
    }

    @Redirect(method = "canBePushed", at = @At("HEAD"))
    public boolean canBePushed() {
        return isAlive() && isOnLadder() && collides;
    }

    @Override
    public boolean getCollides() {
        return collides;
    }

    @Override
    public void setCollides(boolean collidable) {
        collides = collidable;
    }

    @Override
    public MagmaLivingEntity getBukkitEntity() {
        return (MagmaLivingEntity) super.getBukkitEntity();
    }
}
