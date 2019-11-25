package org.magmafoundation.magma.api.bridge;

import net.minecraft.entity.Entity;
import org.magmafoundation.magma.api.core.entity.MagmaEntity;
import org.spongepowered.asm.mixin.Mixin;

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

}
