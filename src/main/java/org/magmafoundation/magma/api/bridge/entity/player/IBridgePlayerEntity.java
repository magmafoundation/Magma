package org.magmafoundation.magma.api.bridge.entity.player;

import net.minecraft.util.text.ITextComponent;

import org.magmafoundation.magma.api.bridge.entity.IBridgeLivingEntity;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;

/**
 * IBridgePlayerEntity
 *
 * @author Redned
 * @since 13/01/2020 - 09:47 pm
 */
public interface IBridgePlayerEntity extends IBridgeLivingEntity {

    ITextComponent getDisplayName();

    void setDisplayName(ITextComponent displayName);

    @Override
    MagmaHumanEntity getBukkitEntity();
}
