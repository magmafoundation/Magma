package org.magmafoundation.magma.api.mixin;

import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;
import org.magmafoundation.magma.api.bridge.IBridgeMinecraftServer;
import org.magmafoundation.magma.api.core.MagmaOptions;
import org.magmafoundation.magma.api.core.MagmaServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MixinMinecraftServer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 05:57 am
 */
@Mixin(MinecraftServer.class)
public class MixinMinecraftServer implements IBridgeMinecraftServer {

    private static MagmaServer magmaServer;
    private static OptionSet options;

    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] p_main_0_, CallbackInfo callbackInfo) {
        options = MagmaOptions.main(p_main_0_);
    }

    @Override
    public MagmaServer getMagmaServer() {
        return magmaServer;
    }

    @Override
    public void setMagmaServer(MagmaServer magmaServer) {
        this.magmaServer = magmaServer;
    }

    @Override
    public OptionSet getOptions() {
        return options;
    }
}
