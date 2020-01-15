package org.magmafoundation.magma.api.mixin.entity.player;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

import org.magmafoundation.magma.api.bridge.entity.player.IBridgeServerPlayerEntity;
import org.magmafoundation.magma.api.core.entity.MagmaPlayer;
import org.spongepowered.asm.mixin.*;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends MixinPlayerEntity implements IBridgeServerPlayerEntity {

    private ITextComponent listName;

    @Shadow public abstract ITextComponent getName();

    @Overwrite
    public ITextComponent getTabListDisplayName() {
        return listName == null ? getName() : listName;
    }

    @Override
    public void setTabListDisplayName(ITextComponent listName) {
        this.listName = listName;
    }

    @Override
    public MagmaPlayer getBukkitEntity() {
        return (MagmaPlayer) super.getBukkitEntity();
    }
}
