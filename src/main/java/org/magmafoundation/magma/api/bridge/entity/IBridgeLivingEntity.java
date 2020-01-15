package org.magmafoundation.magma.api.bridge.entity;

import net.minecraft.entity.player.PlayerEntity;

import org.magmafoundation.magma.api.core.entity.MagmaLivingEntity;

/**
 * IBridgeLivingEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 05:03 pm
 */
public interface IBridgeLivingEntity extends IBridgeEntity {

    int getMaxHurtResistantTime();

    void setMaxHurtResistantTime(int time);

    double getLastDamage();

    void setLastDamage(double damage);

    PlayerEntity getAttackingPlayer();

    boolean getCanPickUpLoot();

    void setCanPickUpLoot(boolean loot);

    boolean getCollides();

    void setCollides(boolean collidable);

    @Override
    MagmaLivingEntity getBukkitEntity();
}
