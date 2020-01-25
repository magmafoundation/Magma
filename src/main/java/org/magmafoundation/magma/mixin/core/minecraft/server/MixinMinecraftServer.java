package org.magmafoundation.magma.mixin.core.minecraft.server;

import joptsimple.OptionSet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import org.magmafoundation.magma.MagmaOptions;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * MixinMinecraftServer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 05:57 am
 */
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Shadow @Final private static org.apache.logging.log4j.Logger LOGGER;

    @Shadow @Final protected Thread serverThread;
    @Shadow @Final protected Map<DimensionType, ServerWorld> worlds;
    @Shadow public abstract void initiateShutdown(boolean p_71263_1_);
    @Shadow protected int serverPort;
    @Shadow protected PlayerList playerList;
    @Shadow protected boolean onlineMode;
    @Shadow protected boolean allowFlight;
    @Shadow protected String motd;
    @Shadow public abstract void shadow$reload();
    @Shadow public abstract int getMaxPlayerIdleMinutes();
    @Shadow public abstract void setPlayerIdleTimeout(int idleTimeout);
    @Shadow protected boolean whitelistEnabled;

    private static OptionSet options;

    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] p_main_0_, CallbackInfo callbackInfo) {
        options = MagmaOptions.main(p_main_0_);
    }

}
