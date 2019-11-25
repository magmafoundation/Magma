package org.magmafoundation.magma.api.bridge;

import net.minecraft.entity.player.PlayerEntity;

/**
 * IBridgeLivingEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 05:03 pm
 */
public interface IBridgeLivingEntity {

    int getMaxHurtResistantTime();

    void setMaxHurtResistantTime(int time);

    double getLastDamage();

    void setLastDamage(double damage);

    PlayerEntity getAttackingPlayer();

}
