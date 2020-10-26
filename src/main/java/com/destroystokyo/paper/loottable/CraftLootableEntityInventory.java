package com.destroystokyo.paper.loottable;

import net.minecraft.world.World;
import org.bukkit.entity.Entity;

public interface CraftLootableEntityInventory extends LootableEntityInventory, CraftLootableInventory {

    net.minecraft.entity.Entity getHandle();

    @Override
    default LootableInventory getAPILootableInventory() {
        return this;
    }

    default Entity getEntity() {
        return getHandle().getBukkitEntity();
    }

    @Override
    default World getNMSWorld() {
        return getHandle().getEntityWorld();
    }

    @Override
    default CraftLootableInventoryData getLootableData() {
        if (getHandle() instanceof CraftLootableInventory) {
            return ((CraftLootableInventory) getHandle()).getLootableData();
        }
        return null;
    }
}
