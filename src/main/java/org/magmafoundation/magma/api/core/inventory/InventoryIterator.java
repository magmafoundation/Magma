package org.magmafoundation.magma.api.core.inventory;

import java.util.ListIterator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * InventoryIterator
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 27/11/2019 - 05:40 am
 */
public class InventoryIterator implements ListIterator<ItemStack> {

    private final Inventory inventory;
    private int nextIndex;
    private Boolean lastDirection; // true = forward, false = backward, null = haven't moved yet

    public InventoryIterator(Inventory inventory) {
        this.inventory = inventory;
        this.nextIndex = 0;
    }

    public InventoryIterator(Inventory inventory, int nextIndex) {
        this.inventory = inventory;
        this.nextIndex = nextIndex;
    }

    @Override
    public boolean hasNext() {
        return nextIndex < inventory.getSize();
    }

    @Override
    public ItemStack next() {
        lastDirection = true;
        return inventory.getItem(nextIndex++);
    }

    @Override
    public int nextIndex() {
        return nextIndex;
    }

    @Override
    public boolean hasPrevious() {
        return nextIndex > 0;
    }

    @Override
    public ItemStack previous() {
        lastDirection = false;
        return inventory.getItem(--nextIndex);
    }

    @Override
    public int previousIndex() {
        return nextIndex - 1;
    }

    @Override
    public void set(ItemStack item) {
        if (lastDirection == null) {
            throw new IllegalStateException("No current item!");
        }
        int i = lastDirection ? nextIndex - 1 : nextIndex;
        inventory.setItem(i, item);
    }

    @Override
    public void add(ItemStack item) {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
}
