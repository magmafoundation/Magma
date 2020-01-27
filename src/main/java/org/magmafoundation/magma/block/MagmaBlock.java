package org.magmafoundation.magma.block;

import com.destroystokyo.paper.block.BlockSoundGroup;

import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * MagmaBlock
 *
 * @author Redned
 * @since 26/01/2020 - 12:05 pm
 */
public class MagmaBlock implements Block {

    private net.minecraft.block.Block block;
    private Location location;

    private net.minecraft.world.World mcWorld;

    public MagmaBlock(net.minecraft.block.Block block, Location location) {
        this.block = block;
        this.location = location;

        this.mcWorld = (net.minecraft.world.World) location.getWorld();
    }

    @Override
    public byte getData() {
        return 0; // TODO
    }

    @NotNull
    @Override
    public BlockData getBlockData() {
        return (BlockData) mcWorld.getBlockState(new BlockPos(getX(), getY(), getZ()));
    }

    @NotNull
    @Override
    public Block getRelative(int modX, int modY, int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace face) {
        return getRelative(face, 1);
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    @NotNull
    @Override
    public Material getType() {
        // TODO: Modded materials
        return Material.matchMaterial(block.getRegistryName().toString());
    }

    @Override
    public byte getLightLevel() {
        return (byte) mcWorld.getLight(new BlockPos(getX(), getY(), getZ()));
    }

    @Override
    public byte getLightFromSky() {
        return (byte) mcWorld.getLightFor(LightType.SKY, new BlockPos(getX(), getY(), getZ()));
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte) mcWorld.getLightFor(LightType.BLOCK, new BlockPos(getX(), getY(), getZ()));
    }

    @NotNull
    @Override
    public World getWorld() {
        return location.getWorld();
    }

    @Override
    public int getX() {
        return location.getBlockX();
    }

    @Override
    public int getY() {
        return location.getBlockY();
    }

    @Override
    public int getZ() {
        return location.getBlockZ();
    }

    @NotNull
    @Override
    public Location getLocation() {
        return location;
    }

    @Nullable
    @Override
    public Location getLocation(@Nullable Location loc) {
        if (loc != null) {
            loc.setWorld(location.getWorld());
            loc.setX(location.getX());
            loc.setY(location.getY());
            loc.setZ(location.getZ());
            loc.setYaw(0);
            loc.setPitch(0);
        }

        return loc;
    }

    @NotNull
    @Override
    public Chunk getChunk() {
        return location.getWorld().getChunkAt(this);
    }

    @Override
    public void setBlockData(@NotNull BlockData data) {
        setBlockData(data, true);
    }

    @Override
    public void setBlockData(@NotNull BlockData data, boolean applyPhysics) {

    }

    @Override
    public void setType(@NotNull Material type) {
        setType(type, true);
    }

    @Override
    public void setType(@NotNull Material type, boolean applyPhysics) {
        setBlockData(type.createBlockData(), applyPhysics);
    }

    @Nullable
    @Override
    public BlockFace getFace(@NotNull Block block) {
        BlockFace[] values = BlockFace.values();
        for (BlockFace face : values) {
            if (getX() + face.getModX() == block.getX() &&
                    getY() + face.getModY() == block.getY() &&
                    getZ() + face.getModZ() == block.getZ()) {
                return face;
            }
        }

        return null;
    }

    @NotNull
    @Override
    public BlockState getState() {
        return null; // TODO
    }

    @NotNull
    @Override
    public BlockState getState(boolean useSnapshot) {
        return null; // TODO
    }

    @NotNull
    @Override
    public Biome getBiome() {
        return getWorld().getBiome(getX(), getZ());
    }

    @Override
    public void setBiome(@NotNull Biome biome) {
        getWorld().setBiome(getX(), getZ(), biome);
    }

    @Override
    public boolean isBlockPowered() {
        return mcWorld.getStrongPower(new BlockPos(getX(), getY(), getZ())) > 0;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return mcWorld.isBlockPowered(new BlockPos(getX(), getY(), getZ()));
    }

    @Override
    public boolean isBlockFacePowered(@NotNull BlockFace face) {
        return mcWorld.isSidePowered(new BlockPos(getX(), getY(), getZ()), Direction.valueOf(face.name()));
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace face) {
        int power = getBlockPower(face);
        Block relative = getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE)
            return Math.max(power, relative.getData()) > 0;

        return power > 0;
    }

    @Override
    public int getBlockPower(@NotNull BlockFace face) {
        return mcWorld.getRedstonePower(new BlockPos(getX(), getY(), getZ()), Direction.valueOf(face.name()));
    }

    @Override
    public int getBlockPower() {
        return getBlockPower(BlockFace.SELF);
    }

    @Override
    public boolean isEmpty() {
        return getType().isEmpty();
    }

    @Override
    public boolean isLiquid() {
        return ((net.minecraft.block.BlockState) getBlockData()).getMaterial().isLiquid();
    }

    @Override
    public double getTemperature() {
        BlockPos pos = new BlockPos(getX(), getY(), getZ());
        return mcWorld.getBiome(pos).getTemperature(pos);
    }

    @Override
    public double getHumidity() {
        return getWorld().getHumidity(getX(), getZ());
    }

    @NotNull
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(((net.minecraft.block.BlockState) getBlockData()).getPushReaction().ordinal());
    }

    @Override
    public boolean breakNaturally() {
        return breakNaturally(new ItemStack(Material.AIR));
    }

    @Override
    public boolean breakNaturally(@NotNull ItemStack tool) {
        boolean result = false;
        if (block != null && block != Blocks.AIR) {
            BlockPos pos = new BlockPos(getX(), getY(), getZ());
            net.minecraft.block.Block.spawnDrops(((net.minecraft.block.BlockState) getBlockData()), mcWorld, pos, mcWorld.getTileEntity(pos), null, (net.minecraft.item.ItemStack) (Object) tool);
            result = true;
        }
        return setTypeAndData(Blocks.AIR.getDefaultState(), true) && result;
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops() {
        return getDrops(new ItemStack(Material.AIR));
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops(@NotNull ItemStack tool) {
        net.minecraft.block.BlockState state = ((net.minecraft.block.BlockState) getBlockData());
        net.minecraft.item.ItemStack mcStack = (net.minecraft.item.ItemStack) (Object) tool;

        if (state.getMaterial().isToolNotRequired() || mcStack.canHarvestBlock(state)) {
            BlockPos pos = new BlockPos(getX(), getY(), getZ());
            return (List<ItemStack>) (Object) net.minecraft.block.Block.getDrops(state, (ServerWorld) mcWorld, pos, mcWorld.getTileEntity(pos), null, mcStack);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isPassable() {
        return ((net.minecraft.block.BlockState) getBlockData()).getCollisionShape(mcWorld, new BlockPos(getX(), getY(), getZ())).isEmpty();
    }

    @Nullable
    @Override
    public RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null; // TODO
    }

    @NotNull
    @Override
    public BoundingBox getBoundingBox() {
        VoxelShape shape = ((net.minecraft.block.BlockState) getBlockData()).getShape(mcWorld, new BlockPos(getX(), getY(), getZ()));
        if (shape.isEmpty())
            return new BoundingBox();

        AxisAlignedBB aabb = shape.getBoundingBox();
        return new BoundingBox(getX() + aabb.minX, getY() + aabb.minY, getZ() + aabb.minZ, getX() + aabb.maxX, getY() + aabb.maxY, getZ() + aabb.maxZ);
    }

    @NotNull
    @Override
    public BlockSoundGroup getSoundGroup() {
        return (BlockSoundGroup) ((net.minecraft.block.BlockState) getBlockData()).getSoundType();
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        // TODO
    }

    @NotNull
    @Override
    public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        return null; // TODO
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return false; // TODO
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        // TODO
    }

    private boolean setTypeAndData(net.minecraft.block.BlockState state, boolean applyPhysics) {
        BlockPos pos = new BlockPos(getX(), getY(), getZ());
        // SPIGOT-611: need to do this to prevent glitchiness. Easier to handle this here (like /setblock) than to fix weirdness in tile entity cleanup
        if (!state.isAir(mcWorld, pos) && state.hasTileEntity() && state.getBlock() != block) {
            mcWorld.removeTileEntity(pos);
        }

        if (applyPhysics) {
            return mcWorld.setBlockState(pos, state, 3);
        } else {
            net.minecraft.block.BlockState old = mcWorld.getBlockState(pos);
            boolean success = mcWorld.setBlockState(pos, state, 2 | 16 | 1024); // NOTIFY | NO_OBSERVER | NO_PLACE (custom)
            if (success) {
                mcWorld.notifyBlockUpdate(pos, old, state, 3);
            }
            return success;
        }
    }
}
