package com.destroystokyo.paper;

import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface HopperPusher {

    default TileEntityHopper findHopper() {
        BlockPos pos = new BlockPos(getX(), getY(), getZ());
        int startX = pos.getX() - 1;
        int endX = pos.getX() + 1;
        int startY = Math.max(0, pos.getY() - 1);
        int endY = Math.min(255, pos.getY() + 1);
        int startZ = pos.getZ() - 1;
        int endZ = pos.getZ() + 1;
        BlockPos.PooledMutableBlockPos adjacentPos = BlockPos.PooledMutableBlockPos.aquire();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    adjacentPos.setValues(x, y, z);
                    TileEntityHopper hopper = MCUtil.getHopper(getWorld(), adjacentPos);
                    if (hopper == null) {
                        continue; // Avoid playing with the bounding boxes, if at all possible
                    }
                    AxisAlignedBB hopperBoundingBox = hopper.getHopperLookupBoundingBox();
                    /*
                     * Check if the entity's bounding box intersects with the hopper's lookup box.
                     * This operation doesn't work both ways!
                     * Make sure you check if the entity's box intersects the hopper's box, not vice versa!
                     */
                    AxisAlignedBB boundingBox = this.getEntityBoundingBox().shrink(0.1); // Imitate vanilla behavior
                    if (boundingBox.intersects(hopperBoundingBox)) {
                        return hopper;
                    }
                }
            }
        }
        adjacentPos.free();
        return null;
    }

    boolean acceptItem(TileEntityHopper hopper);

    default boolean tryPutInHopper() {
        if (!getWorld().paperConfig.isHopperPushBased) {
            return false;
        }
        TileEntityHopper hopper = findHopper();
        return hopper != null && hopper.canAcceptItems() && acceptItem(hopper);
    }

    AxisAlignedBB getEntityBoundingBox();

    World getWorld();

    double getX();

    double getY();

    double getZ();
}
