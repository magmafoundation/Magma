package org.bukkit.craftbukkit.v1_12_R1.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.event.inventory.SaddledHorseInventory;

public class CraftSaddledInventory extends CraftInventoryAbstractHorse implements SaddledHorseInventory {

    public CraftSaddledInventory(IInventory inventory) {
        super(inventory);
    }

}
