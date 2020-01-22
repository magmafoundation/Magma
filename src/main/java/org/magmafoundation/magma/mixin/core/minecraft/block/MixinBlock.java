package org.magmafoundation.magma.mixin.core.minecraft.block;

import com.destroystokyo.paper.block.BlockSoundGroup;
import java.util.Collection;
import java.util.List;
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
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

/**
 * MixinBlock
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/01/2020 - 01:44 am
 */

@Mixin(net.minecraft.block.Block.class)
@Implements(@Interface(iface = Block.class, prefix = "block$"))
public class MixinBlock implements Block {

    @Override
    public byte getData() {
        return 0;
    }

    @NotNull
    @Override
    public BlockData getBlockData() {
        return null;
    }

    @NotNull
    @Override
    public Block getRelative(int modX, int modY, int modZ) {
        return null;
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace face) {
        return null;
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace face, int distance) {
        return null;
    }

    @NotNull
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

    @NotNull
    @Override
    public World getWorld() {
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

    @NotNull
    @Override
    public Location getLocation() {
        return null;
    }

    @Nullable
    @Override
    public Location getLocation(@Nullable Location loc) {
        return null;
    }

    @NotNull
    @Override
    public Chunk getChunk() {
        return null;
    }

    @Override
    public void setBlockData(@NotNull BlockData data) {

    }

    @Override
    public void setBlockData(@NotNull BlockData data, boolean applyPhysics) {

    }

    @Override
    public void setType(@NotNull Material type) {

    }

    @Override
    public void setType(@NotNull Material type, boolean applyPhysics) {

    }

    @Nullable
    @Override
    public BlockFace getFace(@NotNull Block block) {
        return null;
    }

    @NotNull
    @Override
    public BlockState getState() {
        return null;
    }

    @NotNull
    @Override
    public BlockState getState(boolean useSnapshot) {
        return null;
    }

    @NotNull
    @Override
    public Biome getBiome() {
        return null;
    }

    @Override
    public void setBiome(@NotNull Biome bio) {

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
    public boolean isBlockFacePowered(@NotNull BlockFace face) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace face) {
        return false;
    }

    @Override
    public int getBlockPower(@NotNull BlockFace face) {
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

    @NotNull
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    @Override
    public boolean breakNaturally() {
        return false;
    }

    @Override
    public boolean breakNaturally(@NotNull ItemStack tool) {
        return false;
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops() {
        return null;
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops(@NotNull ItemStack tool) {
        return null;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Nullable
    @Override
    public RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @NotNull
    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    @NotNull
    @Override
    public BlockSoundGroup getSoundGroup() {
        return null;
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {

    }

    @NotNull
    @Override
    public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {

    }
}
