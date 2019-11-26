package org.magmafoundation.magma.api.core.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang.Validate;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.magmafoundation.magma.api.accessor.entity.IAccessorEntity;
import org.magmafoundation.magma.api.bridge.potion.IBridgeEffectInstance;
import org.magmafoundation.magma.api.bridge.entity.IBridgeEntity;
import org.magmafoundation.magma.api.bridge.entity.IBridgeLivingEntity;
import org.magmafoundation.magma.api.core.MagmaServer;

/**
 * MagmaLivingEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 08:11 pm
 */
public class MagmaLivingEntity extends MagmaEntity implements LivingEntity {

    // TODO: 25/11/2019 Add MagmaEntityEquipment

    public MagmaLivingEntity(MagmaServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
    }

    @Override
    public double getEyeHeight() {
        return getHandle().getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return getEyeHeight();
    }

    @Override
    public Location getEyeLocation() {
        Location location = getLocation();
        location.setY(location.getY() + getEyeHeight());
        return location;
    }

    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLenght) {
        if (transparent == null) {
            transparent = Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
        }
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<>();
        Iterator<Block> blockIterator = new BlockIterator(this, maxDistance);
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            blocks.add(block);
            if (maxLenght != 0 && blocks.size() > maxLenght) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (!transparent.contains(material)) {
                break;
            }
        }
        return blocks;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 0);
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
        RayTraceResult rayTraceResult = rayTraceBlocks(maxDistance, fluidCollisionMode);
        return (rayTraceResult != null ? rayTraceResult.getHitBlock() : null);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance,
        FluidCollisionMode fluidCollisionMode) {
        Location eyeLocation = getEyeLocation();
        Vector eyeDirection = eyeLocation.getDirection();
        return getWorld()
            .rayTraceBlocks(eyeLocation, eyeDirection, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public int getRemainingAir() {
        return getHandle().getAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        getHandle().setAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return ((IBridgeEntity) getHandle()).getMaxAirTicks();
    }

    @Override
    public void setMaximumAir(int ticks) {
        ((IBridgeEntity) getHandle()).setMaxAirTicks(ticks);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return ((IBridgeLivingEntity) getHandle()).getMaxHurtResistantTime();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        ((IBridgeLivingEntity) getHandle()).setMaxHurtResistantTime(ticks);
    }

    @Override
    public double getLastDamage() {
        return ((IBridgeLivingEntity) getHandle()).getLastDamage();
    }

    @Override
    public void setLastDamage(double damage) {
        ((IBridgeLivingEntity) getHandle()).setLastDamage(damage);
    }

    @Override
    public int getNoDamageTicks() {
        return getHandle().hurtResistantTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        getHandle().hurtResistantTime = ticks;
    }

    @Override
    public Player getKiller() {
        return ((IBridgeLivingEntity) getHandle()).getAttackingPlayer() == null ? null
            : (Player) ((IBridgeEntity) getHandle()).getBukkitEntity();
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
        getHandle().addPotionEffect(
            new EffectInstance(Effect.get(effect.getType().getId()), effect.getDuration(),
                effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
        return false;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = effects.stream().map(this::addPotionEffect)
            .reduce(true, (a, b) -> a && b);
        return success;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return getHandle().isPotionActive(Effect.get(type.getId()));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        EffectInstance effect = getHandle().getActivePotionEffect(Effect.get(type.getId()));
        return (effect == null ? null
            : new PotionEffect(PotionEffectType.getById(Effect.getId(effect.getPotion())),
                effect.getDuration(), effect.getAmplifier(),
                ((IBridgeEffectInstance) effect).isAmbient(), effect.doesShowParticles()));
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        getHandle().removePotionEffect(Effect.get(type.getId()));
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return getHandle().getActivePotionMap().values().stream().filter(
            potionEffect -> PotionEffectType.getById(Effect.getId(potionEffect.getPotion()))
                != null).map(potionEffect -> new PotionEffect(
            PotionEffectType.getById(Effect.getId(potionEffect.getPotion())),
            potionEffect.getDuration(), potionEffect.getAmplifier(),
            ((IBridgeEffectInstance) potionEffect).isAmbient(),
            potionEffect.doesShowParticles())).collect(Collectors.toList());
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return getHandle().canEntityBeSeen(((MagmaEntity) other).getHandle());
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return getHandle() instanceof MobEntity && !((IBridgeEntity) ((MobEntity) getHandle()))
            .getPersistenceRequired();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        if (getHandle() instanceof MobEntity) {
            ((IBridgeEntity) ((net.minecraft.entity.LivingEntity) getHandle()))
                .setPersistenceRequired(!remove);
        }
    }

    @Override
    public EntityEquipment getEquipment() {
        // TODO: 26/11/2019 Implement MagmaEquipment
        return null;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        ((IBridgeLivingEntity) getHandle()).setCanPickUpLoot(pickup);
    }

    @Override
    public boolean getCanPickupItems() {
        return ((IBridgeLivingEntity) getHandle()).getCanPickUpLoot();
    }

    @Override
    public boolean isLeashed() {
        if (!(getHandle() instanceof MobEntity)) {
            return false;
        }
        return ((MobEntity) getHandle()).getLeashHolder() != null;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        if (!isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        }
        return ((IBridgeEntity) ((MobEntity) getHandle()).getLeashHolder()).getBukkitEntity();
    }

    private boolean unleash() {
        if (!isLeashed()) {
            return false;
        }
        ((MobEntity) getHandle()).clearLeashed(true, false);
        return true;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        if ((getHandle() instanceof WitherEntity) || !(getHandle() instanceof MobEntity)) {
            return false;
        }

        if (holder == null) {
            return unleash();
        }

        if (holder.isDead()) {
            return false;
        }

        unleash();
        ((MobEntity) getHandle()).setLeashHolder(((MagmaEntity) holder).getHandle(), true);
        return true;
    }

    @Override
    public boolean isGliding() {
        return ((IAccessorEntity) getHandle()).getFlag(7);
    }

    @Override
    public void setGliding(boolean gliding) {
        ((IAccessorEntity) getHandle()).setFlag(7, gliding);
    }

    @Override
    public boolean isSwimming() {
        return getHandle().isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        getHandle().setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return getHandle().isSpinAttacking();
    }

    @Override
    public boolean isSleeping() {
        return getHandle().isSleeping();
    }

    @Override
    public void setAI(boolean ai) {
        if (getHandle() instanceof MobEntity) {
            ((MobEntity) getHandle()).setNoAI(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return (getHandle() instanceof MobEntity) && ((MobEntity) getHandle()).isAIDisabled();
    }

    @Override
    public void setCollidable(boolean collidable) {
        ((IBridgeLivingEntity) getHandle()).setCollides(collidable);
    }

    @Override
    public boolean isCollidable() {
        return ((IBridgeLivingEntity) getHandle()).getCollides();
    }

    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return null;
    }

    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T memoryValue) {

    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return null;
    }

    @Override
    public void damage(double amount) {
        damage(amount, null);
    }

    @Override
    public void damage(double amount, @Nullable Entity source) {
        DamageSource damageSource = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            damageSource = DamageSource.causePlayerDamage(((MagmaHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            damageSource = DamageSource.causeMobDamage(((MagmaLivingEntity) source).getHandle());
        }

        entity.attackEntityFrom(damageSource, (float) amount);
    }

    @Override
    public double getHealth() {
        return Math.min(Math.max(0, getHandle().getHealth()), getMaxHealth());
    }

    @Override
    public void setHealth(double health) {
        health = (float) health;
        if ((health < 0) || (health > getMaxHealth())) {
            throw new IllegalArgumentException(
                "Health must be between 0 and " + getMaxHealth() + ", but was " + health
                    + ". (attribute base balue: " + getHandle().getAttribute(
                    SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + (
                    this instanceof MagmaPlayer ? ", player:" + getName() + ')' : ')'));
        }

        // setHealth must be set before onDeath to respect events  that may prevent death.
        getHandle().setHealth((float) health);

        if (entity instanceof ServerPlayerEntity && health == 0) {
            ((ServerPlayerEntity) entity).onDeath(DamageSource.GENERIC);
        }
    }

    @Override
    public double getAbsorptionAmount() {
        return getHandle().getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        Preconditions
            .checkArgument(amount >= 0 && Double.isFinite(amount), "amount < 0 or non-finite");
        getHandle().setAbsorptionAmount((float) amount);
    }

    @Override
    public double getMaxHealth() {
        return getHandle().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double health) {
        Validate.isTrue(health > 0, "Max health must be greater than 0");

        getHandle().getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);

        if (getHealth() > health) {
            setHealth(health);
        }
    }

    @Override
    public void resetMaxHealth() {
        setHealth(getHandle().getAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttribute()
            .getDefaultValue());
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

//--------------------------------REMOVE---------------------------=======================//

    @Override
    public boolean isPermissionSet(String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return false;
    }

    @Override
    public boolean hasPermission(String name) {
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value,
        int ticks) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {

    }

    @Override
    public void recalculatePermissions() {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

//----------------------------------------------------------------------=======================//

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile,
        Vector velocity) {
        // TODO: 26/11/2019 Add Once MagmaWorld is done.
        return null;
    }

    @Override
    public net.minecraft.entity.LivingEntity getHandle() {
        return (net.minecraft.entity.LivingEntity) entity;
    }
}
