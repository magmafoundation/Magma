package com.destroystokyo.paper.antixray;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IBlockStatePalette;

public class PacketPlayOutMapChunkInfo {
	private final SPacketChunkData packetPlayOutMapChunk;
	private final Chunk chunk;
	private final int chunkSectionSelector;
	private byte[] data;
	private final int[] bitsPerValue = new int[16];
	private final IBlockStatePalette[] dataPalettes = new IBlockStatePalette[16];
	private final int[] dataBitsIndexes = new int[16];
	private final IBlockState[][] predefinedBlockData = new IBlockState[16][];

	public PacketPlayOutMapChunkInfo(SPacketChunkData packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector) {
		this.packetPlayOutMapChunk = packetPlayOutMapChunk;
		this.chunk = chunk;
		this.chunkSectionSelector = chunkSectionSelector;
	}

	public SPacketChunkData getPacketPlayOutMapChunk() {
		return packetPlayOutMapChunk;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public int getChunkSectionSelector() {
		return chunkSectionSelector;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getBitsPerValue(int chunkSectionIndex) {
		return bitsPerValue[chunkSectionIndex];
	}

	public void setBitsPerValue(int chunkSectionIndex, int bitsPerValue) {
		this.bitsPerValue[chunkSectionIndex] = bitsPerValue;
	}

	public IBlockStatePalette getDataPalette(int chunkSectionIndex) {
		return dataPalettes[chunkSectionIndex];
	}

	public void setDataPalette(int chunkSectionIndex, IBlockStatePalette dataPalette) {
		dataPalettes[chunkSectionIndex] = dataPalette;
	}

	public int getDataBitsIndex(int chunkSectionIndex) {
		return dataBitsIndexes[chunkSectionIndex];
	}

	public void setDataBitsIndex(int chunkSectionIndex, int dataBitsIndex) {
		dataBitsIndexes[chunkSectionIndex] = dataBitsIndex;
	}

	public IBlockState[] getPredefinedBlockData(int chunkSectionIndex) {
		return predefinedBlockData[chunkSectionIndex];
	}

	public void setPredefinedBlockData(int chunkSectionIndex, IBlockState[] predefinedBlockData) {
		this.predefinedBlockData[chunkSectionIndex] = predefinedBlockData;
	}

	public boolean isWritten(int chunkSectionIndex) {
		return bitsPerValue[chunkSectionIndex] != 0;
	}
}
