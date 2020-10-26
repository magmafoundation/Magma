package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an Item.
 */
public interface Item extends Entity {

    /**
     * Gets the item stack associated with this item drop.
     *
     * @return An item stack.
     */
    public ItemStack getItemStack();

    /**
     * Sets the item stack associated with this item drop.
     *
     * @param stack An item stack.
     */
    public void setItemStack(ItemStack stack);

    /**
     * Gets the delay before this Item is available to be picked up by players
     *
     * @return Remaining delay
     */
    public int getPickupDelay();

    /**
     * Sets the delay before this Item is available to be picked up by players
     *
     * @param delay New delay
     */
    public void setPickupDelay(int delay);

    // Paper Start
    /**
     * Gets if non-player entities can pick this Item up
     *
     * @return True if non-player entities can pickup
     */
    public boolean canMobPickup();
    /**
     * Sets if non-player entities can pick this Item up
     *
     * @param canMobPickup True to allow non-player entity pickup
     */
    public void setCanMobPickup(boolean canMobPickup);
    // Paper end
}
