package org.magmafoundation.magma.api.core.block;

import java.util.Collection;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class MagmaBlock implements org.bukkit.block.Block {


    private final IWorld world;
    private final BlockPos position;

    public MagmaBlock(IWorld world, BlockPos position) {
        this.world = world;
        this.position = position;
    }

    public static MagmaBlock at(IWorld world, BlockPos position) {
        return new MagmaBlock(world, position);
    }
    public net.minecraft.block.Block getNMSBlock() {
        return getNMS().getBlock();
    }


    @Override
    public byte getData() {
        net.minecraft.block.BlockState blockState = world.getBlockState(position);
        //TODO: return MagmaMagicNumbers.toLegacyData(blockData);

        //TODO: World patches
        return 0;

    }

    @Override
    public BlockData getBlockData() {
        return null;
    }

    @Override
    public Block getRelative(int modX, int modY, int modZ) {
        return null;
    }

    @Override
    public Block getRelative(BlockFace face) {
        return null;
    }

    @Override
    public Block getRelative(BlockFace face, int distance) {
        return null;
    }

    @Override
    public Material getType() {
        return null;
    }

    @Override
    public byte getLightLevel() {
        return 0;
    }

    @Override
    public byte getLightFromSky() {
        return 0;
    }

    @Override
    public byte getLightFromBlocks() {
        return 0;
    }

    @Override
    public World getWorld() {
        //TODO: World patch
        return null;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Location getLocation(Location loc) {
        return null;
    }

    @Override
    public Chunk getChunk() {
        //TODO: Block patch
        return null;
    }

    @Override
    public void setBlockData(BlockData data) {

    }

    @Override
    public void setBlockData(BlockData data, boolean applyPhysics) {

    }

    @Override
    public void setType(Material type) {

    }

    @Override
    public void setType(Material type, boolean applyPhysics) {

    }

    @Override
    public BlockFace getFace(Block block) {
        return null;
    }

    @Override
    public BlockState getState() {
        return null;
    }

    @Override
    public Biome getBiome() {
        return null;
    }

    @Override
    public void setBiome(Biome bio) {

    }

    @Override
    public boolean isBlockPowered() {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return false;
    }

    @Override
    public boolean isBlockFacePowered(BlockFace face) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        return false;
    }

    @Override
    public int getBlockPower(BlockFace face) {
        return 0;
    }

    @Override
    public int getBlockPower() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isLiquid() {
        return false;
    }

    @Override
    public double getTemperature() {
        return 0;
    }

    @Override
    public double getHumidity() {
        return 0;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    @Override
    public boolean breakNaturally() {
        return false;
    }

    @Override
    public boolean breakNaturally(ItemStack tool) {
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return null;
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack tool) {
        return null;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance,
        FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
    }

    public net.minecraft.block.BlockState getNMS() {
       return world.getBlockState(position);
    }
}
