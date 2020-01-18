package org.magmafoundation.magma.mixin.minecraft.entity;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.*;

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

    @Shadow public boolean onGround;;
    @Shadow public float rotationYaw;
    @Shadow public float rotationPitch;
    @Shadow public float prevRotationYaw;
    @Shadow public float prevRotationPitch;
    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public double posZ;
    @Shadow public net.minecraft.world.World world;
    @Shadow protected UUID entityUniqueID;

    @Shadow public abstract Vec3d getMotion();
    @Shadow public abstract BlockPos getPosition();
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

    private boolean velocityChanged;

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

    @Override
    public void remove() {

    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {

    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {

    }

    @Override
    public Entity getPassenger() {
        return (Entity) getControllingPassenger();
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> getPassengers() {
        return (List<Entity>) (Object) shadow$getPassengers();
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean eject() {
        return false;
    }

    @Override
    public float getFallDistance() {
        return 0;
    }

    @Override
    public void setFallDistance(float distance) {

    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {

    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return entityUniqueID;
    }

    @Override
    public int getTicksLived() {
        return 0;
    }

    @Override
    public void setTicksLived(int value) {

    }

    @Override
    public void playEffect(EntityEffect type) {

    }

    @Override
    public EntityType getType() {
        return null;
    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        return false;
    }

    @Override
    public Entity getVehicle() {
        return null;
    }

    @Intrinsic
    public void entity$setCustomNameVisible(boolean flag) {
        shadow$setCustomNameVisible(flag);
    }

    @Intrinsic
    public boolean entity$isCustomNameVisible() {
        return shadow$isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean flag) {

    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public void setInvulnerable(boolean flag) {

    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void setSilent(boolean flag) {

    }

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public void setGravity(boolean gravity) {

    }

    @Override
    public int getPortalCooldown() {
        return 0;
    }

    @Override
    public void setPortalCooldown(int cooldown) {

    }

    @Override
    public Set<String> getScoreboardTags() {
        return null;
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return false;
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return false;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    @Override
    public BlockFace getFacing() {
        return null;
    }

    @Override
    public Pose getPose() {
        return null;
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

    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {

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
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
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
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {

    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null;
    }
}
