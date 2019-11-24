package org.magmafoundation.magma.api.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import org.magmafoundation.magma.api.bridge.IBridgeMinecraftServer;
import org.magmafoundation.magma.api.core.MagmaServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MixinPlayerList
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 05:54 am
 */
@Mixin(PlayerList.class)
public class MixinPlayerList {

    private MagmaServer magmaServer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(MinecraftServer server, int maxPlayers, CallbackInfo callbackInfo) {
        magmaServer = new MagmaServer(server, (PlayerList) (Object) this);
        ((IBridgeMinecraftServer) server).setMagmaServer(magmaServer);
    }
}
