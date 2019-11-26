package org.magmafoundation.magma.api.core.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.magmafoundation.magma.api.accessor.entity.IAccessorEntity;
import org.magmafoundation.magma.api.bridge.entity.IBridgeEntity;
import org.magmafoundation.magma.api.core.MagmaServer;

/**
 * MagmaEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 08:12 pm
 */
public abstract class MagmaEntity implements org.bukkit.entity.Entity {

    protected final MagmaServer server;
    protected Entity entity;

    public MagmaEntity(MagmaServer server, Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static MagmaEntity getHandle(MagmaServer server, Entity entity) {
        return null;
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), entity.posX, entity.posY, entity.posZ,
            ((IBridgeEntity) entity).getBukkitYaw(), entity.rotationPitch);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.posX);
            loc.setY(entity.posY);
            loc.setZ(entity.posZ);
            loc.setYaw(((IBridgeEntity) entity).getBukkitYaw());
            loc.setPitch(entity.rotationPitch);
        }
        return loc;
    }

    @Override
    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument(velocity != null, "velocity");
        velocity.checkFinite();
        entity.setMotion(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public Vector getVelocity() {
        return new Vector(entity.getMotion().x, entity.getMotion().y, entity.getMotion().z);
    }

    @Override
    public double getHeight() {
        return getHandle().getHeight();
    }

    @Override
    public double getWidth() {
        return getHandle().getWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        AxisAlignedBB bb = getHandle().getBoundingBox();
        return new BoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    @Override
    public boolean isOnGround() {
        if (entity instanceof AbstractArrowEntity) {
            return ((AbstractArrowEntity) entity).onGround;
        }
        return entity.onGround;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        yaw = Location.normalizeYaw(yaw);
        pitch = Location.normalizePitch(pitch);

        entity.rotationYaw = yaw;
        entity.rotationPitch = pitch;
        entity.prevRotationYaw = yaw;
        entity.prevRotationPitch = pitch;
        entity.setHeadRotation(yaw, 0);
    }

    @Override
    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination) {
        return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        return false;
    }

    @Override
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        List<Entity> notchEntityList = entity.world
            .getEntitiesInAABBexcluding(entity, entity.getBoundingBox().grow(x, y, z), null);
        List<org.bukkit.entity.Entity> bukkitEntityList = new ArrayList<>(notchEntityList.size());

        notchEntityList
            .forEach(entity -> bukkitEntityList.add(((IBridgeEntity) entity).getBukkitEntity()));

        return bukkitEntityList;
    }

    @Override
    public int getEntityId() {
        return entity.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return ((IBridgeEntity) entity).getFire();
    }

    @Override
    public int getMaxFireTicks() {
        return ((IBridgeEntity) entity).getFireImmuneTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        entity.setFire(ticks);
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public boolean isDead() {
        return !entity.isAlive();
    }

    @Override
    public boolean isValid() {
        return entity.isAlive() && ((IBridgeEntity) entity).getValid();
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {

    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String getName() {
        return getServer().getName();
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {

    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        return isEmpty() ? null
            : ((IBridgeEntity) getHandle().getPassengers().get(0)).getBukkitEntity();
    }

    @Override
    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");
        if (passenger instanceof MagmaEntity) {
            eject();
            return ((MagmaEntity) passenger).getHandle().startRiding(getHandle());
        } else {
            return false;
        }
    }

    @Override
    public List<org.bukkit.entity.Entity> getPassengers() {
        return Lists.newArrayList(getHandle().getPassengers().stream()
            .map(entity -> ((IBridgeEntity) entity).getBukkitEntity())
            .collect(Collectors.toList()));
    }

    @Override
    public boolean addPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "passanger == null");
        return ((MagmaEntity) passenger).getHandle().startRiding(getHandle(), true);
    }

    @Override
    public boolean removePassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "passenger == null");
        ((MagmaEntity) passenger).getHandle().stopRiding();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return !getHandle().isBeingRidden();
    }

    @Override
    public boolean eject() {
        if (isEmpty()) {
            return false;
        }

        getHandle().removePassengers();
        return true;
    }

    @Override
    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    @Override
    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        setLastDamageCause(event);
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return getLastDamageCause();
    }

    @Override
    public UUID getUniqueId() {
        return getHandle().getUniqueID();
    }

    @Override
    public int getTicksLived() {
        return getHandle().ticksExisted;
    }

    @Override
    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        getHandle().ticksExisted = value;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkArgument(type != null, "type");

        if (type.getApplicable().isInstance(this)) {
            this.getHandle().world.setEntityState(getHandle(), type.getData());
        }
    }

    @Override
    public boolean isInsideVehicle() {
        return getHandle().getRidingEntity() != null;
    }

    @Override
    public boolean leaveVehicle() {
        if (!isInsideVehicle()) {
            return false;
        }
        getHandle().stopRiding();
        return true;
    }

    @Override
    public org.bukkit.entity.Entity getVehicle() {
        if (!isInsideVehicle()) {
            return null;
        }

        return ((IBridgeEntity) getHandle().getRidingEntity()).getBukkitEntity();
    }

    @Override
    public String getCustomName() {
        ITextComponent name = getHandle().getCustomName();

        if (name == null) {
            return null;
        }

        // TODO: 25/11/2019 MagmaChatMessage
        return null;
    }

    @Override
    public void setCustomName(String name) {
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getHandle().setCustomNameVisible(false);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getHandle().getAlwaysRenderNameTagForRender();
    }

    @Override
    public void setGlowing(boolean flag) {
        getHandle().setGlowing(flag);
        Entity e = getHandle();
        if (((IAccessorEntity) e).getFlag(6) != flag) {
            ((IAccessorEntity) e).setFlag(6, flag);
        }
    }

    @Override
    public boolean isGlowing() {
        return getHandle().isGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getHandle().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getHandle().isInvulnerableTo(DamageSource.GENERIC);
    }

    @Override
    public boolean isSilent() {
        return getHandle().isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        getHandle().setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return !getHandle().hasNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return getHandle().timeUntilPortal;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getHandle().timeUntilPortal = cooldown;
    }

    @Override
    public Set<String> getScoreboardTags() {
        return getHandle().getTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return getHandle().addTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return getHandle().removeTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getHandle().getPushReaction().ordinal());
    }

    @Override
    public BlockFace getFacing() {
        // TODO: 25/11/2019 @Jasper Magmablock.notchface
        return null;
    }

    @Override
    public Pose getPose() {
        return Pose.values()[getHandle().getPose().ordinal()];
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
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {

    }

    public Entity getHandle() {
        return entity;
    }

    // TODO: 25/11/2019 Add permissions
}
