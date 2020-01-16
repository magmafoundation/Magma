package org.magmafoundation.magma.api.bridge.entity;

import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;

/**
 * IBridgePlayerEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 07:53 am
 */
public interface IBridgePlayerEntity extends IBridgeLivingEntity {

    boolean getFauxSleeping();

    void setFauxSleeping(boolean sleeping);

    String getSpawnWorld();

    void setSpawnWorld(String spawnWorld);

    int getOldLevel();

    void setOldLevel(int oldLevel);

    @Override
    MagmaHumanEntity getBukkitEntity();
}
