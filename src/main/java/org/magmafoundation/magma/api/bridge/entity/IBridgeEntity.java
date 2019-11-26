package org.magmafoundation.magma.api.bridge.entity;

import org.magmafoundation.magma.api.core.entity.MagmaEntity;

/**
 * IBridgeEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 08:20 pm
 */
public interface IBridgeEntity {

    float getBukkitYaw();

    MagmaEntity getBukkitEntity();

    int getFireImmuneTicks();

    int getFire();

    boolean getValid();

    int getMaxAirTicks();

    void setMaxAirTicks(int ticks);

    boolean getPersistenceRequired();

    void setPersistenceRequired(boolean persistenceRequired);


}
