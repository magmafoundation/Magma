package org.bukkit.event.inventory;

import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public interface ArmoredHorseInventory extends AbstractHorseInventory {

    /**
     * Gets the item in the horse's armor slot.
     *
     * @return the armor item
     */
    ItemStack getArmor();

    /**
     * Sets the item in the horse's armor slot.
     *
     * @param stack the new item
     */
    void setArmor(ItemStack stack);
}
