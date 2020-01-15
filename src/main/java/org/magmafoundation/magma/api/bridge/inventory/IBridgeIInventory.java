package org.magmafoundation.magma.api.bridge.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;

/**
 * IBridgeIInventory
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 27/11/2019 - 06:18 am
 */
public interface IBridgeIInventory {

    int MAX_STACK = 64;

    List<ItemStack> getContents();

    void onOpen(MagmaHumanEntity who);

    void onClose(MagmaHumanEntity who);

    List<HumanEntity> getViewers();

    InventoryHolder getOwner();

    void setMaxStackSize(int size);

    org.bukkit.Location getLocation();

    default IRecipe getCurrentRecipe() {
        return null;
    }

    default void setCurrentRecipe(IRecipe recipe) {
    }
}
