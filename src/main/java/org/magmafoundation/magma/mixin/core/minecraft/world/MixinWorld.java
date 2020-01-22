package org.magmafoundation.magma.mixin.core.minecraft.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldInfo;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(net.minecraft.world.World.class)
public abstract class MixinWorld {

    @Shadow @Final protected WorldInfo worldInfo;
    @Shadow @Final protected AbstractChunkProvider chunkProvider;
    @Shadow @Final public Dimension dimension;
    @Shadow public abstract MinecraftServer shadow$getServer();

}
