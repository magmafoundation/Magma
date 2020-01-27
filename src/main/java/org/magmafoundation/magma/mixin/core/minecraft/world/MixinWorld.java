package org.magmafoundation.magma.mixin.core.minecraft.world;

import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.storage.WorldInfo;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(net.minecraft.world.World.class)
public abstract class MixinWorld {

    @Shadow @Final public List<TileEntity> loadedTileEntityList;
    @Shadow @Final protected WorldInfo worldInfo;
    @Shadow @Final protected AbstractChunkProvider chunkProvider;
    @Shadow @Final public Dimension dimension;
    @Shadow public abstract MinecraftServer shadow$getServer();
    @Shadow public abstract BlockState getBlockState(BlockPos pos);
    @Shadow public abstract int getHeight(Heightmap.Type p_201676_1_, int p_201676_2_, int p_201676_3_);

}
