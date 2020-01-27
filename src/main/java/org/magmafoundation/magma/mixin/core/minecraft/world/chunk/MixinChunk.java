package org.magmafoundation.magma.mixin.core.minecraft.world.chunk;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.magmafoundation.magma.block.MagmaBlock;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * MixinChunk
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/01/2020 - 01:52 am
 */
@Mixin(net.minecraft.world.chunk.Chunk.class)
@Implements(@Interface(iface = Chunk.class, prefix = "chunk$"))
public abstract class MixinChunk implements Chunk {

    //@formatter:off
    @Shadow @Final private ChunkPos pos;
    @Shadow @Final private Map<BlockPos, TileEntity> tileEntities;
    @Shadow @Final private net.minecraft.world.World world;
    @Shadow public abstract ClassInheritanceMultiMap<net.minecraft.entity.Entity>[] getEntityLists();
    @Shadow public abstract long shadow$getInhabitedTime();
    @Shadow public abstract void shadow$setInhabitedTime(long newInhabitedTime);
    //@formatter:on

    @Override
    public int getX() {
        return pos.x;
    }

    @Override
    public int getZ() {
        return pos.z;
    }

    @NotNull
    @Override
    public World getWorld() {
        return (World) world;
    }

    @NotNull
    @Override
    public Block getBlock(int x, int y, int z) {
        return new MagmaBlock(world.getBlockState(new BlockPos(x, y, z)).getBlock(), new Location(getWorld(), x, y, z));
    }

    @NotNull
    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    @NotNull
    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome, boolean includeBiomeTempRain) {
        return null; // TODO
    }

    @NotNull
    @Override
    public Entity[] getEntities() {
        return (Entity[]) this.getEntityLists();
    }

    @NotNull
    @Override
    public BlockState[] getTileEntities(boolean useSnapshot) {
        BlockState[] blockStates = new BlockState[tileEntities.size()];
        TileEntity[] tileEntities = this.tileEntities.values().toArray(new TileEntity[0]);
        for (int i = 0; i < tileEntities.length; i++) {
            TileEntity tileEntity = tileEntities[i];
            blockStates[i] = ((Block) tileEntity.getBlockState().getBlock()).getState();
        }
        return blockStates;
    }

    @Override
    public boolean isLoaded() {
        return getWorld().isChunkLoaded(this);
    }

    @Override
    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    @Override
    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    @Override
    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    @Override
    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        /* 987234911L is determined in net.minecraft.entity.monster.SlimeEntity when seeing if a slime can spawn in a chunk */
        return getRandomWithSeed(987234911L).nextInt(10) == 0;
    }

    public Random getRandomWithSeed(long seed) {
        return new Random(
            world.getSeed() + (long) (this.pos.x * this.pos.x * 4987142) + (long) (this.pos.x * 5947611) + (long) (this.pos.z * this.pos.z) * 4392871L + (long) (this.pos.z * 389711) ^ seed);
    }

    @Override
    public boolean isForceLoaded() {
        return getWorld().isChunkForceLoaded(getX(), getZ());
    }

    @Override
    public void setForceLoaded(boolean forced) {
        getWorld().setChunkForceLoaded(getX(), getZ(), forced);
    }

    @Override
    public boolean addPluginChunkTicket(@NotNull Plugin plugin) {
        return getWorld().addPluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public boolean removePluginChunkTicket(@NotNull Plugin plugin) {
        return getWorld().removePluginChunkTicket(getX(), getZ(), plugin);
    }

    @NotNull
    @Override
    public Collection<Plugin> getPluginChunkTickets() {
        return getWorld().getPluginChunkTickets(getX(), getZ());
    }

    @Intrinsic
    public long chunk$getInhabitedTime() {
        return shadow$getInhabitedTime();
    }

    @Intrinsic
    public void chunk$setInhabitedTime(long ticks) {
        shadow$setInhabitedTime(ticks);
    }

    @Override
    public boolean contains(@NotNull BlockData block) {
        return false;
    }
}
