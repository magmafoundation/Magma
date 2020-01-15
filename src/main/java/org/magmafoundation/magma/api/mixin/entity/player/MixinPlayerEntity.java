package org.magmafoundation.magma.api.mixin.entity.player;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import org.magmafoundation.magma.api.bridge.entity.player.IBridgePlayerEntity;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;
import org.magmafoundation.magma.api.mixin.entity.MixinLivingEntity;
import org.spongepowered.asm.mixin.*;

@Mixin(PlayerEntity.class)
@Implements(@Interface(iface = IBridgePlayerEntity.class, prefix = "implPlayer$"))
public abstract class MixinPlayerEntity extends MixinLivingEntity implements IBridgePlayerEntity {

    private ITextComponent displayName;

    @Shadow
    protected GameProfile gameProfile;

    @Shadow public abstract ITextComponent shadow$getDisplayName();

    @Intrinsic
    public ITextComponent implPlayer$getDisplayName() {
        return displayName == null ? shadow$getDisplayName() : displayName;
    }

    @Override
    public void setDisplayName(ITextComponent displayName) {
        this.displayName = displayName;
    }

    @Override
    public MagmaHumanEntity getBukkitEntity() {
        return (MagmaHumanEntity) super.getBukkitEntity();
    }
}
