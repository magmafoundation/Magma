package org.magmafoundation.magma.api.accessor.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;

/**
 * IMagmaInventory
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 26/11/2019 - 07:23 am
 */
public interface IMagmaInventory extends IInventory {

    int MAX_STACK = 64;

    java.util.List<ItemStack> getContents();

    void onOpen(MagmaHumanEntity who);

    void onClose(MagmaHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    org.bukkit.Location getLocation();

    default IRecipe getCurrentRecipe() {
        return null;
    }

    default void setCurrentRecipe(IRecipe recipe) {
    }

    @Override
    default int getInventoryStackLimit() {
        return 0;
    }
}
