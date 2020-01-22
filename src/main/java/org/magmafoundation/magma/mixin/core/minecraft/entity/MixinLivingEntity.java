package org.magmafoundation.magma.mixin.core.minecraft.entity;

import com.google.common.collect.Sets;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.magmafoundation.magma.bridge.minecraft.entity.BridgeMobEntity;
import org.magmafoundation.magma.util.MagicNumbers;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

/**
 * MixinLivingEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 04:57 pm
 */
@Mixin(net.minecraft.entity.LivingEntity.class)
@Implements(@Interface(iface = LivingEntity.class, prefix = "living$"))
public abstract class MixinLivingEntity extends MixinEntity implements LivingEntity {

    @Shadow @Final private Map<Effect, EffectInstance> activePotionsMap;
    @Shadow protected float lastDamage;
    @Shadow protected PlayerEntity attackingPlayer;

    @Shadow @Final public abstract float shadow$getMaxHealth();
    @Shadow public abstract boolean attackEntityFrom(DamageSource source, float amount);
    @Shadow public abstract boolean addPotionEffect(EffectInstance p_195064_1_);
    @Shadow protected abstract void onFinishedPotionEffect(EffectInstance effect);
    @Shadow public abstract boolean canEntityBeSeen(net.minecraft.entity.Entity entityIn);
    @Shadow public abstract boolean isElytraFlying();
    @Shadow public abstract boolean isSpinAttacking();
    @Shadow public abstract boolean shadow$isSleeping();
    @Shadow public abstract AbstractAttributeMap getAttributes();
    @Shadow public abstract float shadow$getHealth();
    @Shadow public abstract void shadow$setHealth(float health);
    @Shadow public abstract float shadow$getAbsorptionAmount();
    @Shadow public abstract void shadow$setAbsorptionAmount(float amount);

    private int maxNoDamageTicks = 20;
    private boolean canPickUpItems;
    private boolean collidable;

    @Override
    public double getEyeHeight() {
        return shadow$getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return getEyeHeight();
    }

    @Override
    public Location getEyeLocation() {
        return getLocation().clone().add(0D, getLocation().getY() + getEyeHeight(), 0D);
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance) {
        return getTargetBlockExact(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
        RayTraceResult hitResult = rayTraceBlocks(maxDistance, fluidCollisionMode);
        return (hitResult != null ? hitResult.getHitBlock() : null);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
        Location eyeLocation = getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        return getWorld().rayTraceBlocks(eyeLocation, direction, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public int getRemainingAir() {
        return getAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        setAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return maxAir;
    }

    @Override
    public void setMaximumAir(int ticks) {
        this.maxAir = ticks;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return maxNoDamageTicks;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        this.maxNoDamageTicks = ticks;
    }

    @Override
    public double getLastDamage() {
        return lastDamage;
    }

    @Override
    public void setLastDamage(double damage) {
        this.setLastDamage(damage);
    }

    @Override
    public int getNoDamageTicks() {
        return hurtResistantTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        this.hurtResistantTime = ticks;
    }

    @Override
    public Player getKiller() {
        return (Player) attackingPlayer;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            removePotionEffect(effect.getType());
        }
        return addPotionEffect(new EffectInstance(Effect.get(effect.getType().getId()), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return activePotionsMap.containsKey(Effect.get(type.getId()));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        if (!activePotionsMap.containsKey(Effect.get(type.getId())))
            return null;

        EffectInstance instance = activePotionsMap.get(Effect.get(type.getId()));
        return new PotionEffect(type, instance.getDuration(), instance.getAmplifier(), instance.isAmbient(), instance.doesShowParticles(), instance.isShowIcon());
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        if (!activePotionsMap.containsKey(Effect.get(type.getId())))
            return;

        EffectInstance instance = activePotionsMap.get(Effect.get(type.getId()));
        onFinishedPotionEffect(instance);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        Collection<PotionEffect> activeEffects = new ArrayList<>();
        for (EffectInstance instance : activePotionsMap.values()) {
            activeEffects.add(new PotionEffect(PotionEffectType.getById(Effect.getId(instance.getPotion())), instance.getDuration(), instance.getAmplifier(), instance.isAmbient(), instance.doesShowParticles(), instance.isShowIcon()));
        }
        return activeEffects;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return canEntityBeSeen((net.minecraft.entity.Entity) other);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return (Entity) this instanceof MobEntity && ((MobEntity) (Entity) this).isNoDespawnRequired();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        if (!((Entity) this instanceof MobEntity))
            return;

        ((BridgeMobEntity) this).bridge$setPersistenceRequired(!remove);
    }

    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        this.canPickUpItems = pickup;
    }

    @Override
    public boolean getCanPickupItems() {
        return canPickUpItems;
    }

    @Override
    public boolean isLeashed() {
        if (!((Entity) this instanceof MobEntity))
            return false;

        return ((MobEntity) (Entity) this).getLeashed();
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        if (!isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        }

        return (Entity) ((MobEntity) (Entity) this).getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        if (((Entity) this instanceof WitherEntity) || !((Entity) this instanceof MobEntity))
            return false;

        if (holder == null)
            unleash();

        if (holder.isDead())
            return false;

        unleash();
        ((MobEntity) (Entity) this).setLeashHolder((net.minecraft.entity.Entity) holder, true);
        return true;
    }

    @Override
    public boolean isGliding() {
        return isElytraFlying();
    }

    @Override
    public void setGliding(boolean gliding) {
        setFlag(MagicNumbers.ENTITY_FLAG_GLIDING, gliding);
    }

    @Intrinsic
    public boolean living$isSwimming() {
        return shadow$isSwimming();
    }

    @Intrinsic
    public void living$setSwimming(boolean swimming) {
        shadow$setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return isSpinAttacking();
    }

    @Intrinsic
    public boolean living$isSleeping() {
        return shadow$isSleeping();
    }

    @Override
    public void setAI(boolean ai) {
        if ((Entity) this instanceof MobEntity) {
            ((MobEntity) (Entity) this).setNoAI(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return ((Entity) this instanceof MobEntity) && !((MobEntity) (Entity) this).isAIDisabled();
    }

    @Override
    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    @Override
    public boolean isCollidable() {
        return collidable;
    }

    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return null; // TODO
    }

    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T memoryValue) {
        // TODO
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        switch (attribute) {
            case GENERIC_ARMOR:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.ARMOR);
            case GENERIC_ARMOR_TOUGHNESS:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.ARMOR_TOUGHNESS);
            case GENERIC_ATTACK_DAMAGE:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
            case GENERIC_ATTACK_SPEED:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED);
            case GENERIC_FLYING_SPEED:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.FLYING_SPEED);
            case GENERIC_FOLLOW_RANGE:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE);
            case GENERIC_KNOCKBACK_RESISTANCE:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
            case GENERIC_LUCK:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.LUCK);
            case GENERIC_MAX_HEALTH:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
            case GENERIC_MOVEMENT_SPEED:
                return (AttributeInstance) getAttributes().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
        }
        return null;
    }

    @Override
    public void damage(double amount) {
        damage(amount, null);
    }

    @Override
    public void damage(double amount, Entity source) {
        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.causePlayerDamage(((PlayerEntity) source));
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.causeMobDamage(((net.minecraft.entity.LivingEntity) source));
        }

        attackEntityFrom(reason, (float) amount);
    }

    @Override
    public double getHealth() {
        return shadow$getHealth();
    }

    @Override
    public void setHealth(double health) {
        shadow$setHealth((float) health);
    }

    @Override
    public double getAbsorptionAmount() {
        return shadow$getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        shadow$setAbsorptionAmount((float) amount);
    }

    @Override
    public double getMaxHealth() {
        return shadow$getMaxHealth();
    }

    @Override
    public void setMaxHealth(double health) {
        getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
    }

    @Override
    public void resetMaxHealth() {
        getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return null; // TODO
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return null; // TODO
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        if (transparent == null) {
            transparent = Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
        }
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        List<Block> blocks = new ArrayList<>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (!transparent.contains(material)) {
                break;
            }
        }
        return blocks;
    }

    private boolean unleash() {
        if (!isLeashed()) {
            return false;
        }
        ((MobEntity) (Entity) this).clearLeashed(true, false);
        return true;
    }

    @Inject(method = "canBeCollidedWith", at = @At("RETURN"))
    private void onCanBeCollidedWith(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && collidable);
    }

    @Inject(method = "canBePushed", at = @At("RETURN"))
    private void onCanBePushed(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && collidable);
    }
}
