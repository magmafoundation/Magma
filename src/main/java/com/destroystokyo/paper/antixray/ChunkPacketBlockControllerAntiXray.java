package com.destroystokyo.paper.antixray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.destroystokyo.paper.PaperWorldConfig;
import org.bukkit.World.Environment;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.BlockStatePaletteRegistry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkPacketBlockControllerAntiXray extends ChunkPacketBlockController {
	private static ExecutorService executorServiceInstance = null;
	private final ExecutorService executorService;
	private final boolean asynchronous;
	private final EngineMode engineMode;
	private final ChunkEdgeMode chunkEdgeMode;
	private final int maxChunkSectionIndex;
	private final int updateRadius;
	private final IBlockState[] predefinedBlockData;
	private final IBlockState[] predefinedBlockDataStone;
	private final IBlockState[] predefinedBlockDataNetherrack;
	private final IBlockState[] predefinedBlockDataEndStone;
	private final int[] predefinedBlockDataBits;
	private final int[] predefinedBlockDataBitsGlobal;
	private final int[] predefinedBlockDataBitsStoneGlobal;
	private final int[] predefinedBlockDataBitsNetherrackGlobal;
	private final int[] predefinedBlockDataBitsEndStoneGlobal;
	private final boolean[] solidGlobal = new boolean[Block.BLOCK_STATE_IDS.size()]; // May need to change to GameRegistry
	private final boolean[] obfuscateGlobal = new boolean[Block.BLOCK_STATE_IDS.size()]; // May need to change to GameRegistry
	private final ExtendedBlockStorage[] emptyNearbyChunkSections = {Chunk.NULL_BLOCK_STORAGE, Chunk.NULL_BLOCK_STORAGE, Chunk.NULL_BLOCK_STORAGE, Chunk.NULL_BLOCK_STORAGE};

	public ChunkPacketBlockControllerAntiXray(PaperWorldConfig paperWorldConfig) {
		asynchronous = paperWorldConfig.asynchronous;
		engineMode = paperWorldConfig.engineMode;
		chunkEdgeMode = paperWorldConfig.chunkEdgeMode;
		maxChunkSectionIndex = paperWorldConfig.maxChunkSectionIndex;
		updateRadius = paperWorldConfig.updateRadius;
		if (asynchronous) {
			executorService = getExecutorServiceInstance();
		} else {
			executorService = null;
		}
		List<Object> toObfuscate;
		if (engineMode == EngineMode.HIDE) {
			toObfuscate = paperWorldConfig.hiddenBlocks;
			predefinedBlockData = null;
			predefinedBlockDataStone = new IBlockState[] {Blocks.STONE.getDefaultState()};
			predefinedBlockDataNetherrack = new IBlockState[] {Blocks.NETHERRACK.getDefaultState()};
			predefinedBlockDataEndStone = new IBlockState[] {Blocks.END_STONE.getDefaultState()};
			predefinedBlockDataBits = new int[] {1};
			predefinedBlockDataBitsGlobal = null;
			predefinedBlockDataBitsStoneGlobal = new int[] {Block.BLOCK_STATE_IDS.get(Blocks.STONE.getDefaultState())};
			predefinedBlockDataBitsNetherrackGlobal = new int[] {Block.BLOCK_STATE_IDS.get(Blocks.NETHERRACK.getDefaultState())};
			predefinedBlockDataBitsEndStoneGlobal = new int[] {Block.BLOCK_STATE_IDS.get(Blocks.END_STONE.getDefaultState())};
		} else {
			toObfuscate = new ArrayList<>(paperWorldConfig.replacementBlocks);
			Set<IBlockState> predefinedBlockDataSet = new HashSet<IBlockState>();
			for (Object id : paperWorldConfig.hiddenBlocks) {
				Block block = Block.getBlockFromName(String.valueOf(id));
				if (block != null && !block.hasTileEntity()) {
					toObfuscate.add(id);
					predefinedBlockDataSet.add(block.getDefaultState());
				}
			}
			predefinedBlockData = predefinedBlockDataSet.size() == 0 ? new IBlockState[] {Blocks.DIAMOND_ORE.getDefaultState()} : predefinedBlockDataSet.toArray(new IBlockState[predefinedBlockDataSet.size()]);
			predefinedBlockDataStone = null;
			predefinedBlockDataNetherrack = null;
			predefinedBlockDataEndStone = null;
			predefinedBlockDataBits = new int[predefinedBlockData.length];
			predefinedBlockDataBitsGlobal = new int[predefinedBlockData.length];
			boolean containsDefaultBlockData = false;
			for (int i = 0; i < predefinedBlockData.length; i++) {
				predefinedBlockDataBits[i] = containsDefaultBlockData ? i : (containsDefaultBlockData = predefinedBlockData[i] == BlockStateContainer.DEFAULT_BLOCK_DATA) ? 0 : i + 1;
				predefinedBlockDataBitsGlobal[i] = Block.BLOCK_STATE_IDS.get(predefinedBlockData[i]);
			}
			predefinedBlockDataBitsStoneGlobal = null;
			predefinedBlockDataBitsNetherrackGlobal = null;
			predefinedBlockDataBitsEndStoneGlobal = null;
		}
		for (Object id : toObfuscate) {
			Block block = Block.getBlockFromName(String.valueOf(id));
			if (block != null) {
				obfuscateGlobal[Block.BLOCK_STATE_IDS.get(block.getDefaultState())] = true;
			}
		}
		for (int i = 0; i < solidGlobal.length; i++) {
			IBlockState blockData = Block.BLOCK_STATE_IDS.getByValue(i);
			if (blockData != null) {
				solidGlobal[i] = blockData.getBlock().isNormalCube(blockData) && blockData.getBlock() != Blocks.MOB_SPAWNER && blockData.getBlock() != Blocks.BARRIER;
			}
		}
	}

	private static ExecutorService getExecutorServiceInstance() {
		if (executorServiceInstance == null) {
			executorServiceInstance = Executors.newSingleThreadExecutor();
		}
		return executorServiceInstance;
	}

	@Override
	public IBlockState[] getPredefinedBlockData(Chunk chunk, int chunkSectionIndex) {
		//Return the block data which should be added to the data palettes so that they can be used for the obfuscation
		if (chunkSectionIndex <= maxChunkSectionIndex) {
			switch (engineMode) {
			case HIDE:
				switch (chunk.world.getWorld().getEnvironment()) {
				case NETHER:
					return predefinedBlockDataNetherrack;
				case THE_END:
					return predefinedBlockDataEndStone;
				default:
					return predefinedBlockDataStone;
				}
			default:
				return predefinedBlockData;
			}
		}
		return null;
	}

	@Override
	public boolean onChunkPacketCreate(Chunk chunk, int chunkSectionSelector, boolean force) {
		//Load nearby chunks if necessary
		if (chunkEdgeMode == ChunkEdgeMode.WAIT && !force) {
			if (chunk.world.getChunkIfLoaded(chunk.x - 1, chunk.z) == null || chunk.world.getChunkIfLoaded(chunk.x + 1, chunk.z) == null || chunk.world.getChunkIfLoaded(chunk.x, chunk.z - 1) == null || chunk.world.getChunkIfLoaded(chunk.x, chunk.z + 1) == null) {
				//Don't create the chunk packet now, wait until nearby chunks are loaded and create it later
				return false;
			}
		} else if (chunkEdgeMode == ChunkEdgeMode.LOAD || chunkEdgeMode == ChunkEdgeMode.WAIT) {
			chunk.world.getChunkFromChunkCoords(chunk.x - 1, chunk.z);
			chunk.world.getChunkFromChunkCoords(chunk.x + 1, chunk.z);
			chunk.world.getChunkFromChunkCoords(chunk.x, chunk.z - 1);
			chunk.world.getChunkFromChunkCoords(chunk.x, chunk.z + 1);
		}
		//Create the chunk packet now
		return true;
	}

	@Override
	public PacketPlayOutMapChunkInfoAntiXray getPacketPlayOutMapChunkInfo(SPacketChunkData packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector) {
		//Return a new instance to collect data and objects in the right state while creating the chunk packet for thread safe access later
		PacketPlayOutMapChunkInfoAntiXray packetPlayOutMapChunkInfoAntiXray = new PacketPlayOutMapChunkInfoAntiXray(packetPlayOutMapChunk, chunk, chunkSectionSelector, this);
		packetPlayOutMapChunkInfoAntiXray.setNearbyChunks(chunk.world.getChunkIfLoaded(chunk.x - 1, chunk.z), chunk.world.getChunkIfLoaded(chunk.x + 1, chunk.z), chunk.world.getChunkIfLoaded(chunk.x, chunk.z - 1), chunk.world.getChunkIfLoaded(chunk.x, chunk.z + 1));
		return packetPlayOutMapChunkInfoAntiXray;
	}

	@Override
	public void modifyBlocks(SPacketChunkData packetPlayOutMapChunk, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo) {
		if (asynchronous) {
			executorService.submit((PacketPlayOutMapChunkInfoAntiXray) packetPlayOutMapChunkInfo);
		} else {
			obfuscate((PacketPlayOutMapChunkInfoAntiXray) packetPlayOutMapChunkInfo);
		}
	}

	//Actually these fields should be variables inside the obfuscate method but in sync mode or with SingleThreadExecutor in async mode it's okay
	private final boolean[] solid = new boolean[Block.BLOCK_STATE_IDS.size()];
	private final boolean[] obfuscate = new boolean[Block.BLOCK_STATE_IDS.size()];
	//These boolean arrays represent chunk layers, true means don't obfuscate, false means obfuscate
	private boolean[][] current = new boolean[16][16];
	private boolean[][] next = new boolean[16][16];
	private boolean[][] nextNext = new boolean[16][16];
	private final DataBitsReader dataBitsReader = new DataBitsReader();
	private final DataBitsWriter dataBitsWriter = new DataBitsWriter();
	private final ExtendedBlockStorage[] nearbyChunkSections = new ExtendedBlockStorage[4];

	public void obfuscate(PacketPlayOutMapChunkInfoAntiXray packetPlayOutMapChunkInfoAntiXray) {
		boolean[] solidTemp = null;
		boolean[] obfuscateTemp = null;
		dataBitsReader.setDataBits(packetPlayOutMapChunkInfoAntiXray.getData());
		dataBitsWriter.setDataBits(packetPlayOutMapChunkInfoAntiXray.getData());
		int counter = 0;
		for (int chunkSectionIndex = 0; chunkSectionIndex <= maxChunkSectionIndex; chunkSectionIndex++) {
			if (packetPlayOutMapChunkInfoAntiXray.isWritten(chunkSectionIndex) && packetPlayOutMapChunkInfoAntiXray.getPredefinedBlockData(chunkSectionIndex) != null) {
				int[] predefinedBlockDataBitsTemp = packetPlayOutMapChunkInfoAntiXray.getDataPalette(chunkSectionIndex) instanceof BlockStatePaletteRegistry ? engineMode == EngineMode.HIDE ? packetPlayOutMapChunkInfoAntiXray.getChunk().world.getWorld().getEnvironment() == Environment.NETHER ? predefinedBlockDataBitsNetherrackGlobal : packetPlayOutMapChunkInfoAntiXray.getChunk().world.getWorld().getEnvironment() == Environment.THE_END ? predefinedBlockDataBitsEndStoneGlobal : predefinedBlockDataBitsStoneGlobal : predefinedBlockDataBitsGlobal : predefinedBlockDataBits;
				dataBitsWriter.setIndex(packetPlayOutMapChunkInfoAntiXray.getDataBitsIndex(chunkSectionIndex));
				//Check if the chunk section below was not obfuscated
				if (chunkSectionIndex == 0 || !packetPlayOutMapChunkInfoAntiXray.isWritten(chunkSectionIndex - 1) || packetPlayOutMapChunkInfoAntiXray.getPredefinedBlockData(chunkSectionIndex - 1) == null) {
					//If so, initialize some stuff
					dataBitsReader.setBitsPerValue(packetPlayOutMapChunkInfoAntiXray.getBitsPerValue(chunkSectionIndex));
					dataBitsReader.setIndex(packetPlayOutMapChunkInfoAntiXray.getDataBitsIndex(chunkSectionIndex));
					solidTemp = readDataPalette(packetPlayOutMapChunkInfoAntiXray.getDataPalette(chunkSectionIndex), solid, solidGlobal);
					obfuscateTemp = readDataPalette(packetPlayOutMapChunkInfoAntiXray.getDataPalette(chunkSectionIndex), obfuscate, obfuscateGlobal);
					//Read the blocks of the upper layer of the chunk section below if it exists
					ExtendedBlockStorage belowChunkSection = null;
					boolean skipFirstLayer = chunkSectionIndex == 0 || (belowChunkSection = packetPlayOutMapChunkInfoAntiXray.getChunk().getBlockStorageArray()[chunkSectionIndex - 1]) == Chunk.NULL_BLOCK_STORAGE;
					for (int z = 0; z < 16; z++) {
						for (int x = 0; x < 16; x++) {
							current[z][x] = true;
							next[z][x] = skipFirstLayer || !solidGlobal[Block.BLOCK_STATE_IDS.get(belowChunkSection.get(x, 15, z))];
						}
					}
					//Abuse the obfuscateLayer method to read the blocks of the first layer of the current chunk section
					dataBitsWriter.setBitsPerValue(0);
					obfuscateLayer(-1, dataBitsReader, dataBitsWriter, solidTemp, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, emptyNearbyChunkSections, counter);
				}
				dataBitsWriter.setBitsPerValue(packetPlayOutMapChunkInfoAntiXray.getBitsPerValue(chunkSectionIndex));
				nearbyChunkSections[0] = packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[0] == null ? Chunk.NULL_BLOCK_STORAGE : packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[0].getBlockStorageArray()[chunkSectionIndex];
				nearbyChunkSections[1] = packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[1] == null ? Chunk.NULL_BLOCK_STORAGE : packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[1].getBlockStorageArray()[chunkSectionIndex];
				nearbyChunkSections[2] = packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[2] == null ? Chunk.NULL_BLOCK_STORAGE : packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[2].getBlockStorageArray()[chunkSectionIndex];
				nearbyChunkSections[3] = packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[3] == null ? Chunk.NULL_BLOCK_STORAGE : packetPlayOutMapChunkInfoAntiXray.getNearbyChunks()[3].getBlockStorageArray()[chunkSectionIndex];
				//Obfuscate all layers of the current chunk section except the upper one
				for (int y = 0; y < 15; y++) {
					boolean[][] temp = current;
					current = next;
					next = nextNext;
					nextNext = temp;
					counter = obfuscateLayer(y, dataBitsReader, dataBitsWriter, solidTemp, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, nearbyChunkSections, counter);
				}
				//Check if the chunk section above doesn't need obfuscation
				if (chunkSectionIndex == maxChunkSectionIndex || !packetPlayOutMapChunkInfoAntiXray.isWritten(chunkSectionIndex + 1) || packetPlayOutMapChunkInfoAntiXray.getPredefinedBlockData(chunkSectionIndex + 1) == null) {
					//If so, obfuscate the upper layer of the current chunk section by reading blocks of the first layer from the chunk section above if it exists
					ExtendedBlockStorage aboveChunkSection;
					if (chunkSectionIndex != 15 && (aboveChunkSection = packetPlayOutMapChunkInfoAntiXray.getChunk().getBlockStorageArray()[chunkSectionIndex + 1]) != Chunk.NULL_BLOCK_STORAGE) {
						boolean[][] temp = current;
						current = next;
						next = nextNext;
						nextNext = temp;
						for (int z = 0; z < 16; z++) {
							for (int x = 0; x < 16; x++) {
								if (!solidGlobal[Block.BLOCK_STATE_IDS.get(aboveChunkSection.get(x, 0, z))]) {
									current[z][x] = true;
								}
							}
						}
						//There is nothing to read anymore
						dataBitsReader.setBitsPerValue(0);
						solid[0] = true;
						counter = obfuscateLayer(15, dataBitsReader, dataBitsWriter, solid, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, nearbyChunkSections, counter);
					}
				} else {
					//If not, initialize the reader and other stuff for the chunk section above to obfuscate the upper layer of the current chunk section
					dataBitsReader.setBitsPerValue(packetPlayOutMapChunkInfoAntiXray.getBitsPerValue(chunkSectionIndex + 1));
					dataBitsReader.setIndex(packetPlayOutMapChunkInfoAntiXray.getDataBitsIndex(chunkSectionIndex + 1));
					solidTemp = readDataPalette(packetPlayOutMapChunkInfoAntiXray.getDataPalette(chunkSectionIndex + 1), solid, solidGlobal);
					obfuscateTemp = readDataPalette(packetPlayOutMapChunkInfoAntiXray.getDataPalette(chunkSectionIndex + 1), obfuscate, obfuscateGlobal);
					boolean[][] temp = current;
					current = next;
					next = nextNext;
					nextNext = temp;
					counter = obfuscateLayer(15, dataBitsReader, dataBitsWriter, solidTemp, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, nearbyChunkSections, counter);
				}
				dataBitsWriter.finish();
			}
		}
		packetPlayOutMapChunkInfoAntiXray.getPacketPlayOutMapChunk().setReady(true);
	}

	private int obfuscateLayer(int y, DataBitsReader dataBitsReader, DataBitsWriter dataBitsWriter, boolean[] solid, boolean[] obfuscate, int[] predefinedBlockDataBits, boolean[][] current, boolean[][] next, boolean[][] nextNext, ExtendedBlockStorage[] nearbyChunkSections, int counter) {
		//First block of first line
		int dataBits = dataBitsReader.read();
		if (nextNext[0][0] = !solid[dataBits]) {
			dataBitsWriter.skip();
			next[0][1] = true;
			next[1][0] = true;
		} else {
			if (nearbyChunkSections[2] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[2].get(0, y, 15))] || nearbyChunkSections[0] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[0].get(15, y, 0))] || current[0][0]) {
				dataBitsWriter.skip();
			} else {
				if (counter >= predefinedBlockDataBits.length) {
					counter = 0;
				}
				dataBitsWriter.write(predefinedBlockDataBits[counter++]);
			}
		}
		if (!obfuscate[dataBits]) {
			next[0][0] = true;
		}
		//First line
		for (int x = 1; x < 15; x++) {
			dataBits = dataBitsReader.read();
			if (nextNext[0][x] = !solid[dataBits]) {
				dataBitsWriter.skip();
				next[0][x - 1] = true;
				next[0][x + 1] = true;
				next[1][x] = true;
			} else {
				if (nearbyChunkSections[2] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[2].get(x, y, 15))] || current[0][x]) {
					dataBitsWriter.skip();
				} else {
					if (counter >= predefinedBlockDataBits.length) {
						counter = 0;
					}
					dataBitsWriter.write(predefinedBlockDataBits[counter++]);
				}
			}
			if (!obfuscate[dataBits]) {
				next[0][x] = true;
			}
		}
		//Last block of first line
		dataBits = dataBitsReader.read();
		if (nextNext[0][15] = !solid[dataBits]) {
			dataBitsWriter.skip();
			next[0][14] = true;
			next[1][15] = true;
		} else {
			if (nearbyChunkSections[2] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[2].get(15, y, 15))] || nearbyChunkSections[1] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[1].get(0, y, 0))] || current[0][15]) {
				dataBitsWriter.skip();
			} else {
				if (counter >= predefinedBlockDataBits.length) {
					counter = 0;
				}
				dataBitsWriter.write(predefinedBlockDataBits[counter++]);
			}
		}
		if (!obfuscate[dataBits]) {
			next[0][15] = true;
		}
		//All inner lines
		for (int z = 1; z < 15; z++) {
			//First block
			dataBits = dataBitsReader.read();
			if (nextNext[z][0] = !solid[dataBits]) {
				dataBitsWriter.skip();
				next[z][1] = true;
				next[z - 1][0] = true;
				next[z + 1][0] = true;
			} else {
				if (nearbyChunkSections[0] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[0].get(15, y, z))] || current[z][0]) {
					dataBitsWriter.skip();
				} else {
					if (counter >= predefinedBlockDataBits.length) {
						counter = 0;
					}
					dataBitsWriter.write(predefinedBlockDataBits[counter++]);
				}
			}
			if (!obfuscate[dataBits]) {
				next[z][0] = true;
			}
			//All inner blocks
			for (int x = 1; x < 15; x++) {
				dataBits = dataBitsReader.read();
				if (nextNext[z][x] = !solid[dataBits]) {
					dataBitsWriter.skip();
					next[z][x - 1] = true;
					next[z][x + 1] = true;
					next[z - 1][x] = true;
					next[z + 1][x] = true;
				} else {
					if (current[z][x]) {
						dataBitsWriter.skip();
					} else {
						if (counter >= predefinedBlockDataBits.length) {
							counter = 0;
						}
						dataBitsWriter.write(predefinedBlockDataBits[counter++]);
					}
				}
				if (!obfuscate[dataBits]) {
					next[z][x] = true;
				}
			}
			//Last block
			dataBits = dataBitsReader.read();
			if (nextNext[z][15] = !solid[dataBits]) {
				dataBitsWriter.skip();
				next[z][14] = true;
				next[z - 1][15] = true;
				next[z + 1][15] = true;
			} else {
				if (nearbyChunkSections[1] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[1].get(0, y, z))] || current[z][15]) {
					dataBitsWriter.skip();
				} else {
					if (counter >= predefinedBlockDataBits.length) {
						counter = 0;
					}
					dataBitsWriter.write(predefinedBlockDataBits[counter++]);
				}
			}
			if (!obfuscate[dataBits]) {
				next[z][15] = true;
			}
		}
		//First block of last line
		dataBits = dataBitsReader.read();
		if (nextNext[15][0] = !solid[dataBits]) {
			dataBitsWriter.skip();
			next[15][1] = true;
			next[14][0] = true;
		} else {
			if (nearbyChunkSections[3] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[3].get(0, y, 0))] || nearbyChunkSections[0] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[0].get(15, y, 15))] || current[15][0]) {
				dataBitsWriter.skip();
			} else {
				if (counter >= predefinedBlockDataBits.length) {
					counter = 0;
				}
				dataBitsWriter.write(predefinedBlockDataBits[counter++]);
			}
		}
		if (!obfuscate[dataBits]) {
			next[15][0] = true;
		}
		//Last line
		for (int x = 1; x < 15; x++) {
			dataBits = dataBitsReader.read();
			if (nextNext[15][x] = !solid[dataBits]) {
				dataBitsWriter.skip();
				next[15][x - 1] = true;
				next[15][x + 1] = true;
				next[14][x] = true;
			} else {
				if (nearbyChunkSections[3] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[3].get(x, y, 0))] || current[15][x]) {
					dataBitsWriter.skip();
				} else {
					if (counter >= predefinedBlockDataBits.length) {
						counter = 0;
					}
					dataBitsWriter.write(predefinedBlockDataBits[counter++]);
				}
			}
			if (!obfuscate[dataBits]) {
				next[15][x] = true;
			}
		}
		//Last block of last line
		dataBits = dataBitsReader.read();
		if (nextNext[15][15] = !solid[dataBits]) {
			dataBitsWriter.skip();
			next[15][14] = true;
			next[14][15] = true;
		} else {
			if (nearbyChunkSections[3] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[3].get(15, y, 0))] || nearbyChunkSections[1] == Chunk.NULL_BLOCK_STORAGE || !solidGlobal[Block.BLOCK_STATE_IDS.get(nearbyChunkSections[1].get(0, y, 15))] || current[15][15]) {
				dataBitsWriter.skip();
			} else {
				if (counter >= predefinedBlockDataBits.length) {
					counter = 0;
				}
				dataBitsWriter.write(predefinedBlockDataBits[counter++]);
			}
		}
		if (!obfuscate[dataBits]) {
			next[15][15] = true;
		}
		return counter;
	}

	private boolean[] readDataPalette(IBlockStatePalette dataPalette, boolean[] temp, boolean[] global) {
		if (dataPalette instanceof BlockStatePaletteRegistry) {
			return global;
		}
		IBlockState blockData;
		for (int i = 0; (blockData = dataPalette.getBlockState(i)) != null; i++) {
			temp[i] = global[Block.BLOCK_STATE_IDS.get(blockData)];
		}
		return temp;
	}

	@Override
	public void updateNearbyBlocks(World world, BlockPos blockPosition) {
		if (updateRadius >= 2) {
			BlockPos temp = blockPosition.west();
			updateBlock(world, temp);
			updateBlock(world, temp.west());
			updateBlock(world, temp.down());
			updateBlock(world, temp.up());
			updateBlock(world, temp.north());
			updateBlock(world, temp.south());
			updateBlock(world, temp = blockPosition.east());
			updateBlock(world, temp.east());
			updateBlock(world, temp.down());
			updateBlock(world, temp.up());
			updateBlock(world, temp.north());
			updateBlock(world, temp.south());
			updateBlock(world, temp = blockPosition.down());
			updateBlock(world, temp.down());
			updateBlock(world, temp.north());
			updateBlock(world, temp.south());
			updateBlock(world, temp = blockPosition.up());
			updateBlock(world, temp.up());
			updateBlock(world, temp.north());
			updateBlock(world, temp.south());
			updateBlock(world, temp = blockPosition.north());
			updateBlock(world, temp.north());
			updateBlock(world, temp = blockPosition.south());
			updateBlock(world, temp.south());
		} else if (updateRadius == 1) {
			updateBlock(world, blockPosition.west());
			updateBlock(world, blockPosition.east());
			updateBlock(world, blockPosition.down());
			updateBlock(world, blockPosition.up());
			updateBlock(world, blockPosition.north());
			updateBlock(world, blockPosition.south());
		} else {
			//Do nothing if updateRadius <= 0 (test mode)
		}
	}

	private void updateBlock(World world, BlockPos blockPosition) {
		if (world.isBlockLoaded(blockPosition)) {
			IBlockState blockData = world.getBlockState(blockPosition);
			if (obfuscateGlobal[Block.BLOCK_STATE_IDS.get(blockData)]) {
				world.notifyBlockUpdate(blockPosition, blockData, blockData, 3);
			}
		}
	}

	public enum EngineMode {
		HIDE(1, "hide ores"),
		OBFUSCATE(2, "obfuscate");
		private final int id;
		private final String description;

		EngineMode(int id, String description) {
			this.id = id;
			this.description = description;
		}

		public static EngineMode getById(int id) {
			for (EngineMode engineMode : values()) {
				if (engineMode.id == id) {
					return engineMode;
				}
			}
			return null;
		}

		public int getId() {
			return id;
		}

		public String getDescription() {
			return description;
		}
	}

	public enum ChunkEdgeMode {
		DEFAULT(1, "default"),
		WAIT(2, "wait until nearby chunks are loaded"),
		LOAD(3, "load nearby chunks");
		private final int id;
		private final String description;

		ChunkEdgeMode(int id, String description) {
			this.id = id;
			this.description = description;
		}

		public static ChunkEdgeMode getById(int id) {
			for (ChunkEdgeMode chunkEdgeMode : values()) {
				if (chunkEdgeMode.id == id) {
					return chunkEdgeMode;
				}
			}
			return null;
		}

		public int getId() {
			return id;
		}

		public String getDescription() {
			return description;
		}
	}
}
