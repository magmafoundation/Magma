package org.magmafoundation.magma.api.bridge.entity.player;

import net.minecraft.util.text.ITextComponent;

import org.magmafoundation.magma.api.core.entity.MagmaPlayer;

/**
 * IBridgeServerPlayerEntity
 *
 * @author Redned
 * @since 13/01/2020 - 09:38 pm
 */
public interface IBridgeServerPlayerEntity extends IBridgePlayerEntity {

    ITextComponent getTabListDisplayName();

    void setTabListDisplayName(ITextComponent listName);

    @Override
    MagmaPlayer getBukkitEntity();
}
