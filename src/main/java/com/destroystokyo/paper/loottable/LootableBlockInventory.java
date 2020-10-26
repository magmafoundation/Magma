package com.destroystokyo.paper.loottable;

import org.bukkit.block.Block;

public interface LootableBlockInventory extends LootableInventory {

    /**
     * Gets the block that is lootable
     * @return The Block
     */
    Block getBlock();
}
