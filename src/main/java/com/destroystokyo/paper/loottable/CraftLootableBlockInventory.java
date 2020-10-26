package com.destroystokyo.paper.loottable;

import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public interface CraftLootableBlockInventory extends LootableBlockInventory, CraftLootableInventory {

    TileEntityLockableLoot getTileEntity();

    @Override
    default LootableInventory getAPILootableInventory() {
        return this;
    }

    @Override
    default World getNMSWorld() {
        return getTileEntity().getWorld();
    }

    default Block getBlock() {
        final BlockPos position = getTileEntity().getPos();
        final Chunk bukkitChunk = getTileEntity().getWorld().getChunkFromBlockCoords(position).bukkitChunk;
        return bukkitChunk.getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    default CraftLootableInventoryData getLootableData() {
        return getTileEntity().getLootableData();
    }
}
