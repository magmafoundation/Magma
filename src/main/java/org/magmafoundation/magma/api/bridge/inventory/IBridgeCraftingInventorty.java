package org.magmafoundation.magma.api.bridge.inventory;

import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;

/**
 * IBridgeCraftingInventorty
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 06:10 am
 */
public interface IBridgeCraftingInventorty {

    void onOpen(MagmaHumanEntity who);

    void onClose(MagmaHumanEntity who);

    List<HumanEntity> getViewers();

    List<ItemStack> getContents();

    InventoryType getInventoryType();

    InventoryHolder getOwner();

    void setOwner(PlayerEntity playerEntity);

    IRecipe getCurrentRecipe();

    void setCurrentRecipe(IRecipe currentRecipe);

    IInventory getResultInventory();

    void setResultInventory(IInventory resultInventory);

    int getMaxStack();

    void setMaxStack(int maxStack);

}
