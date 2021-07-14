package com.destroystokyo.paper.antixray;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;


public class ChunkPacketBlockController {

	public static final ChunkPacketBlockController NO_OPERATION_INSTANCE = new ChunkPacketBlockController();

	protected ChunkPacketBlockController() {

	}

	public IBlockState[] getPredefinedBlockData(Chunk chunk, int chunkSectionIndex) {
		return null;
	}

	public boolean onChunkPacketCreate(Chunk chunk, int chunkSectionSelector, boolean force) {
		return true;
	}

	public PacketPlayOutMapChunkInfo getPacketPlayOutMapChunkInfo(SPacketChunkData packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector) {
		return null;
	}

	public void modifyBlocks(SPacketChunkData packetPlayOutMapChunk, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo) {
		packetPlayOutMapChunk.setReady(true);
	}

	public void updateNearbyBlocks(World world, BlockPos blockPosition) {

	}
}
