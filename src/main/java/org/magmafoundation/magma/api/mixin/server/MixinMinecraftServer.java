package org.magmafoundation.magma.api.mixin.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.SharedConstants;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import org.magmafoundation.magma.api.bridge.server.IBridgeMinecraftServer;
import org.magmafoundation.magma.api.core.MagmaOptions;
import org.magmafoundation.magma.api.core.MagmaServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    public List<ServerWorld> serverWorldList = new ArrayList<>();

    private static MinecraftServer instance;

    public File bukkitDataPackFolder;


    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfoReturnable<Boolean> cir) {
        instance = (MinecraftServer) (Object) this;
    }

    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] p_main_0_, CallbackInfo callbackInfo) {
        options = MagmaOptions.main(p_main_0_);
    }

    @Inject(method = "loadDataPacks(Ljava/io/File;Lnet/minecraft/world/storage/WorldInfo;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;datapackFinder:Lnet/minecraft/resources/FolderPackFinder;", shift = Shift.AFTER))
    private void loadDataPacks(File file, WorldInfo worldInfo, CallbackInfo ci) {
        bukkitDataPackFolder = new File(new File(file, "datapacks"), "bukkit");
        if (!bukkitDataPackFolder.exists()) {
            bukkitDataPackFolder.mkdirs();
        }
        File mcMeta = new File(bukkitDataPackFolder, "pack.mcmeta");
        try {
            com.google.common.io.Files.write("{\n"
                + "    \"pack\": {\n"
                + "        \"description\": \"Data pack for resources provided by Bukkit plugins\",\n"
                + "        \"pack_format\": " + SharedConstants.getVersion().getPackVersion() + "\n"
                + "    }\n"
                + "}\n", mcMeta, com.google.common.base.Charsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Could not initialize Bukkit datapack", ex);
        }
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

    @Override
    public List<ServerWorld> getServerWorldList() {
        return serverWorldList;
    }

    @Override
    public MinecraftServer getInstance() {
        return instance;
    }

    @Override
    public File getBukkitDataPackFolder() {
        return bukkitDataPackFolder;
    }

    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;setGuiEnabled()V"))
    private static void guiEnabled(DedicatedServer dedicatedServer) {
        // Turns of gui all the time.
        // TODO: 24/11/2019 Make this a setting in configs 
    }

}
