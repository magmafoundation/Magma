package com.destroystokyo.paper.loottable;

import net.minecraft.world.World;

interface CraftLootable extends Lootable {

    World getNMSWorld();

    default org.bukkit.World getBukkitWorld() {
        return getNMSWorld().getWorld();
    }
}
