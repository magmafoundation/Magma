package org.magmafoundation.magma.mixin.core.minecraft.entity;

import net.minecraft.block.material.PushReaction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * MixinEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 08:19 pm
 */
@Mixin(net.minecraft.entity.Entity.class)
@Implements(@Interface(iface = Entity.class, prefix = "entity$"))
public abstract class MixinEntity implements Entity {

    protected static PermissibleBase permissibleBase = new PermissibleBase(new ServerOperator() {

        @Override
        public boolean isOp() {
            return false;
        }

        @Override
        public void setOp(boolean value) {

        }
    });

    @Shadow public boolean onGround;

    @Shadow public float rotationYaw;
    @Shadow public float rotationPitch;
    @Shadow public float prevRotationYaw;
    @Shadow public float prevRotationPitch;
    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public double posZ;
    @Shadow public net.minecraft.world.World world;
    @Shadow protected UUID entityUniqueID;
    @Shadow private net.minecraft.entity.Entity ridingEntity;
    @Shadow public boolean removed;
    @Shadow public float fallDistance;
    @Shadow public int ticksExisted;
    @Shadow public int timeUntilPortal;

    @Shadow public abstract Vec3d getMotion();
    @Shadow public abstract Vec3d getPositionVector();
    @Shadow public abstract void setMotion(Vec3d p_213317_1_);
    @Shadow public abstract AxisAlignedBB shadow$getBoundingBox();
    @Shadow public abstract void setRotationYawHead(float rotation);
    @Shadow public abstract double shadow$getHeight();
    @Shadow public abstract double shadow$getWidth();
    @Shadow public abstract int shadow$getEntityId();
    @Shadow public abstract List<net.minecraft.entity.Entity> shadow$getPassengers();
    @Shadow public abstract net.minecraft.entity.Entity getControllingPassenger();
    @Shadow public abstract ITextComponent shadow$getCustomName();
    @Shadow public abstract void shadow$setCustomName(ITextComponent name);
    @Shadow public abstract boolean shadow$isCustomNameVisible();
    @Shadow public abstract boolean shadow$setCustomNameVisible(boolean alwaysRenderNameTag);
    @Shadow public abstract ITextComponent shadow$getName();
    @Shadow public abstract net.minecraft.entity.EntityType<?> shadow$getType();
    @Shadow public abstract void stopRiding();
    @Shadow public abstract boolean shadow$isGlowing();
    @Shadow public abstract void shadow$setGlowing(boolean glowingIn);
    @Shadow public abstract void shadow$sendMessage(ITextComponent component);
    @Shadow public abstract void shadow$remove();
    @Shadow public abstract boolean shadow$isInvulnerable();
    @Shadow public abstract void shadow$setInvulnerable(boolean isInvulnerable);
    @Shadow public abstract boolean shadow$isSilent();
    @Shadow public abstract void shadow$setSilent(boolean isSilent);
    @Shadow public abstract boolean hasNoGravity();
    @Shadow public abstract boolean setNoGravity(boolean noGravity);
    @Shadow public abstract Set<String> getTags();
    @Shadow public abstract boolean addTag(String tag);
    @Shadow public abstract boolean removeTag(String tag);
    @Shadow public abstract PushReaction getPushReaction();
    @Shadow public abstract net.minecraft.entity.Pose shadow$getPose();
    @Shadow protected abstract boolean getFlag(int flag);
    @Shadow protected abstract void setFlag(int flag, boolean set);

    private boolean velocityChanged;
    private boolean persistent = true;
    private EntityDamageEvent lastDamageCause;

    @Override
    public Location getLocation() {
        return new Location((World) world, getPositionVector().getX(), getPositionVector().getY(), getPositionVector().getZ());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld((World) world);
            loc.setX(posX);
            loc.setY(posY);
            loc.setZ(posZ);
            loc.setYaw(rotationYaw);
            loc.setPitch(rotationPitch);
        }
        return loc;
    }

    @Override
    public void setVelocity(Vector velocity) {
        velocity.checkFinite();
        setMotion(new Vec3d(velocity.getX(), velocity.getY(), velocity.getZ()));
        velocityChanged = true;
    }

    @Override
    public Vector getVelocity() {
        return new Vector(getMotion().getX(), getMotion().getY(), getMotion().getZ());
    }

    @Intrinsic
    public double entity$getHeight() {
        return shadow$getHeight();
    }

    @Intrinsic
    public double entity$getWidth() {
        return shadow$getWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        AxisAlignedBB boundingBox = shadow$getBoundingBox();
        return new BoundingBox(boundingBox.minX, boundingBox.minY, boundingBox.minZ,
                boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public World getWorld() {
        return (World) world;
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        this.rotationYaw = Location.normalizeYaw(yaw);
        this.rotationPitch = Location.normalizePitch(pitch);

        this.prevRotationYaw = yaw;
        this.prevRotationPitch = pitch;
        setRotationYawHead(yaw);
    }

    @Override
    public boolean teleport(Location location) {
        return teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    @Override
    public boolean teleport(Entity destination) {
        return false;
    }

    @Override
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return null;
    }

    @Intrinsic
    public int entity$getEntityId() {
        return shadow$getEntityId();
    }

    @Override
    public int getFireTicks() {
        return 0;
    }

    @Override
    public int getMaxFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks(int ticks) {

    }

    @Intrinsic
    public void entity$remove() {
        shadow$remove();
    }

    @Override
    public boolean isDead() {
        return removed;
    }

    @Override
    public boolean isValid() {
        return !isDead() && getWorld().isChunkLoaded(getLocation().getBlockX() >> 4, getLocation().getBlockZ() >> 4);
    }

    @Override
    public void sendMessage(String message) {
        shadow$sendMessage(new StringTextComponent(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages)
            sendMessage(message);
    }

    @Override
    public Server getServer() {
        return (Server) world.getServer();
    }

    @Override
    public String getName() {
        return shadow$getName().getFormattedText();
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    @Override
    public Entity getPassenger() {
        return (Entity) getControllingPassenger();
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        return addPassenger(passenger);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> getPassengers() {
        return (List<Entity>) (Object) shadow$getPassengers();
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        return ((net.minecraft.entity.Entity) passenger).startRiding((net.minecraft.entity.Entity) (Entity) this, true);
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        if (!getPassengers().contains(passenger))
            return false;

        return passenger.leaveVehicle();
    }

    @Override
    public boolean isEmpty() {
        return shadow$getPassengers().isEmpty();
    }

    @Override
    public boolean eject() {
        if (isEmpty())
            return false;

        ((net.minecraft.entity.Entity) (Entity) this).removePassengers();
        return false;
    }

    @Override
    public float getFallDistance() {
        return fallDistance;
    }

    @Override
    public void setFallDistance(float distance) {
        this.setFallDistance(distance);
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        this.lastDamageCause = event;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return lastDamageCause;
    }

    @Override
    public UUID getUniqueId() {
        return entityUniqueID;
    }

    @Override
    public int getTicksLived() {
        return ticksExisted;
    }

    @Override
    public void setTicksLived(int value) {
        this.ticksExisted = value;
    }

    @Override
    public void playEffect(EntityEffect type) {
        world.setEntityState(((net.minecraft.entity.Entity) (Entity) this), type.getData());
    }

    @Override
    public EntityType getType() {
        return EntityType.valueOf(net.minecraft.entity.EntityType.getKey(shadow$getType()).getPath().toUpperCase());
    }

    @Override
    public boolean isInsideVehicle() {
        return ridingEntity != null;
    }

    @Override
    public boolean leaveVehicle() {
        if (isInsideVehicle())
            return false;

        stopRiding();
        return true;
    }

    @Override
    public Entity getVehicle() {
        return (Entity) ridingEntity;
    }

    @Intrinsic
    public void entity$setCustomNameVisible(boolean flag) {
        shadow$setCustomNameVisible(flag);
    }

    @Intrinsic
    public boolean entity$isCustomNameVisible() {
        return shadow$isCustomNameVisible();
    }

    @Intrinsic
    public void entity$setGlowing(boolean flag) {
        shadow$setGlowing(flag);
    }

    @Intrinsic
    public boolean entity$isGlowing() {
        return shadow$isGlowing();
    }

    @Intrinsic
    public void entity$setInvulnerable(boolean flag) {
        shadow$setInvulnerable(flag);
    }

    @Intrinsic
    public boolean entity$isInvulnerable() {
        return shadow$isInvulnerable();
    }

    @Intrinsic
    public boolean entity$isSilent() {
        return shadow$isSilent();
    }

    @Intrinsic
    public void setSilent(boolean flag) {
        shadow$setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return !hasNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return timeUntilPortal;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        this.timeUntilPortal = cooldown;
    }

    @Override
    public Set<String> getScoreboardTags() {
        return getTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return addTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return removeTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getPushReaction().ordinal());
    }

    @Override
    public BlockFace getFacing() {
        return null;
    }

    @Override
    public Pose getPose() {
        return Pose.values()[shadow$getPose().ordinal()];
    }

    @Override
    public String getCustomName() {
        return shadow$getCustomName().getFormattedText();
    }

    @Override
    public void setCustomName(String name) {
        shadow$setCustomName(new StringTextComponent(name));
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        // TODO
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null; // TODO
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return false; // TODO
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        // TODO
    }

    @Override
    public boolean isPermissionSet(String name) {
        return permissibleBase.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return permissibleBase.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return permissibleBase.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return permissibleBase.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return permissibleBase.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return permissibleBase.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return permissibleBase.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return permissibleBase.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        permissibleBase.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        permissibleBase.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return permissibleBase.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return permissibleBase.isOp();
    }

    @Override
    public void setOp(boolean value) {
        permissibleBase.setOp(value);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null; // TODO
    }

    @Inject(method = "writeUnlessRemoved", at = @At("HEAD"), cancellable = true)
    public void onWriteUnlessRemoved(CompoundNBT compound, CallbackInfoReturnable<Boolean> cir) {
        if (!persistent)
            cir.setReturnValue(false);
    }
}
