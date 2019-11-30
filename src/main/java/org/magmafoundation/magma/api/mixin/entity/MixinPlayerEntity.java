package org.magmafoundation.magma.api.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;
import org.magmafoundation.magma.api.bridge.entity.IBridgePlayerEntity;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * MixinPlayerEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 07:54 am
 */
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends MixinLivingEntity implements IBridgePlayerEntity {

    public boolean fauxSleeping;
    public String spawnWorld = "";
    public int oldLevel = -1;

    @Override
    public boolean getFauxSleeping() {
        return fauxSleeping;
    }

    @Override
    public void setFauxSleeping(boolean sleeping) {
        this.fauxSleeping = sleeping;
    }

    @Override
    public String getSpawnWorld() {
        return spawnWorld;
    }

    @Override
    public void setSpawnWorld(String spawnWorld) {
        this.spawnWorld = spawnWorld;
    }

    @Override
    public int getOldLevel() {
        return oldLevel;
    }

    @Override
    public void setOldLevel(int oldLevel) {
        this.oldLevel = oldLevel;
    }

    @Override
    public MagmaHumanEntity getBukkitEntity() {
        return (MagmaHumanEntity) super.getBukkitEntity();
    }
}
