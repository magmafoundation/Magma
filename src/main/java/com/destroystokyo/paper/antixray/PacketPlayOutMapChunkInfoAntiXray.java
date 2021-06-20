package com.destroystokyo.paper.antixray;

import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;

public class PacketPlayOutMapChunkInfoAntiXray extends PacketPlayOutMapChunkInfo implements Runnable {
	private Chunk[] nearbyChunks;
	private final ChunkPacketBlockControllerAntiXray chunkPacketBlockControllerAntiXray;

	public PacketPlayOutMapChunkInfoAntiXray(SPacketChunkData packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector, ChunkPacketBlockControllerAntiXray chunkPacketBlockControllerAntiXray) {
		super(packetPlayOutMapChunk, chunk, chunkSectionSelector);
		this.chunkPacketBlockControllerAntiXray = chunkPacketBlockControllerAntiXray;
	}

	public Chunk[] getNearbyChunks() {
		return nearbyChunks;
	}

	public void setNearbyChunks(Chunk... nearbyChunks) {
		this.nearbyChunks = nearbyChunks;
	}

	@Override
	public void run() {
		chunkPacketBlockControllerAntiXray.obfuscate(this);
	}
}
