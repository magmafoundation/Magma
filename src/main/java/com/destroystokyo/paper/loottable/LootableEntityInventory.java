package com.destroystokyo.paper.loottable;

import org.bukkit.entity.Entity;

public interface LootableEntityInventory extends LootableInventory {

    /**
     * Gets the entity that is lootable
     *
     * @return The Entity
     */
    Entity getEntity();
}
