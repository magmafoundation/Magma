package org.magmafoundation.magma.api.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.WhiteList;
import net.minecraft.server.management.WhitelistEntry;
import org.magmafoundation.magma.api.bridge.IBridgeMinecraftServer;
import org.magmafoundation.magma.api.core.MagmaServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow
    @Final
    private WhiteList whiteListedPlayers;
    private MagmaServer magmaServer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(MinecraftServer server, int maxPlayers, CallbackInfo callbackInfo) {
        magmaServer = new MagmaServer(server, (PlayerList) (Object) this);
        ((IBridgeMinecraftServer) server).setMagmaServer(magmaServer);
    }

    public void removePlayerFromWhitelist(GameProfile profile) {
        this.whiteListedPlayers.removeEntry(profile);
    }

    public void addPlayerToWhitelist(GameProfile profile) {
        this.whiteListedPlayers.addEntry(new WhitelistEntry(profile));
    }

}
