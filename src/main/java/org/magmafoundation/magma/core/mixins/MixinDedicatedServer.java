package org.magmafoundation.magma.core.mixins;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MixinDedicatedServer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 02:44 am
 */
@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {

    @Inject(method = "init", at = @At("HEAD"))
    private void s(CallbackInfoReturnable<Boolean> callbackInfo) {
        System.out.println("Starting Magma");
    }

}
