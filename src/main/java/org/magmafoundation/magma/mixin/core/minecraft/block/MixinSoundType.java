package org.magmafoundation.magma.mixin.core.minecraft.block;

import com.destroystokyo.paper.block.BlockSoundGroup;

import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvent;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinSoundType
 *
 * @author Redned
 * @since 26/01/2019 - 1:43 pm
 */
@Mixin(SoundType.class)
public abstract class MixinSoundType implements BlockSoundGroup {

    @Shadow @Final private SoundEvent breakSound;
    @Shadow @Final private SoundEvent stepSound;
    @Shadow @Final private SoundEvent placeSound;
    @Shadow @Final private SoundEvent hitSound;
    @Shadow @Final private SoundEvent fallSound;

    @NotNull
    @Override
    public Sound getBreakSound() {
        return Sound.valueOf(breakSound.getName().getPath());
    }

    @NotNull
    @Override
    public Sound getStepSound() {
        return Sound.valueOf(stepSound.getName().getPath());
    }

    @NotNull
    @Override
    public Sound getPlaceSound() {
        return Sound.valueOf(placeSound.getName().getPath());
    }

    @NotNull
    @Override
    public Sound getHitSound() {
        return Sound.valueOf(hitSound.getName().getPath());
    }

    @NotNull
    @Override
    public Sound getFallSound() {
        return Sound.valueOf(fallSound.getName().getPath());
    }
}
