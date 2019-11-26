package org.magmafoundation.magma.api.mixin.potion;

import net.minecraft.potion.EffectInstance;
import org.magmafoundation.magma.api.bridge.potion.IBridgeEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinEffectInstance
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 05:36 pm
 */
@Mixin(EffectInstance.class)
public class MixinEffectInstance implements IBridgeEffectInstance {

    @Shadow
    private boolean ambient;

    @Override
    public boolean isAmbient() {
        return ambient;
    }
}
