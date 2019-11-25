package org.magmafoundation.magma.api.core.entity;

import com.google.common.collect.Sets;
import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
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
import org.magmafoundation.magma.api.bridge.IBridgeEffectInstance;
import org.magmafoundation.magma.api.bridge.IBridgeEntity;
import org.magmafoundation.magma.api.bridge.IBridgeLivingEntity;
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
        return getEntity().getEyeHeight();
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
        return getEntity().getAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        getEntity().setAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return ((IBridgeEntity) getEntity()).getMaxAirTicks();
    }

    @Override
    public void setMaximumAir(int ticks) {
        ((IBridgeEntity) getEntity()).setMaxAirTicks(ticks);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return ((IBridgeLivingEntity) getEntity()).getMaxHurtResistantTime();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        ((IBridgeLivingEntity) getEntity()).setMaxHurtResistantTime(ticks);
    }

    @Override
    public double getLastDamage() {
        return ((IBridgeLivingEntity) getEntity()).getLastDamage();
    }

    @Override
    public void setLastDamage(double damage) {
        ((IBridgeLivingEntity) getEntity()).setLastDamage(damage);
    }

    @Override
    public int getNoDamageTicks() {
        return getEntity().hurtResistantTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        getEntity().hurtResistantTime = ticks;
    }

    @Override
    public Player getKiller() {
        return ((IBridgeLivingEntity) getEntity()).getAttackingPlayer() == null ? null
            : (Player) ((IBridgeEntity) getEntity()).getBukkitEntity();
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
        getEntity().addPotionEffect(
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
        return getEntity().isPotionActive(Effect.get(type.getId()));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        EffectInstance effect = getEntity().getActivePotionEffect(Effect.get(type.getId()));
        return (effect == null ? null
            : new PotionEffect(PotionEffectType.getById(Effect.getId(effect.getPotion())),
                effect.getDuration(), effect.getAmplifier(),
                ((IBridgeEffectInstance) effect).isAmbient(), effect.doesShowParticles()));
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        getEntity().removePotionEffect(Effect.get(type.getId()));
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return getEntity().getActivePotionMap().values().stream().filter(
            potionEffect -> PotionEffectType.getById(Effect.getId(potionEffect.getPotion()))
                != null).map(potionEffect -> new PotionEffect(
            PotionEffectType.getById(Effect.getId(potionEffect.getPotion())),
            potionEffect.getDuration(), potionEffect.getAmplifier(),
            ((IBridgeEffectInstance) potionEffect).isAmbient(),
            potionEffect.doesShowParticles())).collect(Collectors.toList());
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {

    }

    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {

    }

    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        return false;
    }

    @Override
    public boolean isGliding() {
        return false;
    }

    @Override
    public void setGliding(boolean gliding) {

    }

    @Override
    public boolean isSwimming() {
        return false;
    }

    @Override
    public void setSwimming(boolean swimming) {

    }

    @Override
    public boolean isRiptiding() {
        return false;
    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public void setAI(boolean ai) {

    }

    @Override
    public boolean hasAI() {
        return false;
    }

    @Override
    public void setCollidable(boolean collidable) {

    }

    @Override
    public boolean isCollidable() {
        return false;
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

    }

    @Override
    public void damage(double amount, Entity source) {

    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public void setHealth(double health) {

    }

    @Override
    public double getAbsorptionAmount() {
        return 0;
    }

    @Override
    public void setAbsorptionAmount(double amount) {

    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public void setMaxHealth(double health) {

    }

    @Override
    public void resetMaxHealth() {

    }

    @Override
    public EntityType getType() {
        return null;
    }

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

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile,
        Vector velocity) {
        return null;
    }

    @Override
    public net.minecraft.entity.LivingEntity getEntity() {
        return (net.minecraft.entity.LivingEntity) entity;
    }
}
