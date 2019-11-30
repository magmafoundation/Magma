package org.magmafoundation.magma.api.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.magmafoundation.magma.api.bridge.entity.IBridgeEntity;
import org.magmafoundation.magma.api.core.MagmaServer;
import org.magmafoundation.magma.api.core.entity.MagmaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 08:19 pm
 */
@Mixin(Entity.class)
public class MixinEntity implements IBridgeEntity {

    @Shadow
    public float rotationYaw;

    @Shadow
    private int fire;

    @Shadow public World world;
    protected MagmaEntity magmaEntity;
    public boolean valid;

    public int maxAirTicks = 300;

    public boolean persist = true;

    @Override
    public float getBukkitYaw() {
        return rotationYaw;
    }

    @Override
    public MagmaEntity getBukkitEntity() {
        if (magmaEntity == null) {
            magmaEntity = MagmaEntity
                .getHandle((MagmaServer) Bukkit.getServer(), (Entity) (Object) this);
        }
        return magmaEntity;
    }

    @Override
    public int getFireImmuneTicks() {
        return -1;
    }

    @Override
    public int getFire() {
        return fire;
    }

    @Override
    public boolean getValid() {
        return valid;
    }

    @Override
    public int getMaxAirTicks() {
        return maxAirTicks;
    }

    @Override
    public void setMaxAirTicks(int ticks) {
        maxAirTicks = ticks;
    }

    @Override
    public boolean getPersistenceRequired() {
        return persist;
    }

    @Override
    public void setPersistenceRequired(boolean persistenceRequired) {
        persist = persistenceRequired;
    }

}
