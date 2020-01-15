package org.magmafoundation.magma.api.core.inventory;

import java.util.Arrays;
import java.util.List;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.magmafoundation.magma.api.bridge.inventory.IBridgeIInventory;
import org.magmafoundation.magma.api.bridge.item.crafting.IBridgeIRecipe;

/**
 * MagmaInventoryCrafting
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 07:18 am
 */
public class MagmaInventoryCrafting extends MagmaInventory implements CraftingInventory {

    private final IInventory resultInventory;

    public MagmaInventoryCrafting(IInventory inventory,
        IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getMatrixInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getResultInventory().getSizeInventory() + getMatrixInventory().getSizeInventory();
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[getSize()];
        List<net.minecraft.item.ItemStack> mcResultItems = ((IBridgeIInventory) getResultInventory())
            .getContents();

        int i = 0;
        for (i = 0; i < mcResultItems.size(); i++) {
            items[i] = MagmaItemStack.asCraftMirror(mcResultItems.get(i));
        }

        List<net.minecraft.item.ItemStack> mcItems = ((IBridgeIInventory) getMatrixInventory())
            .getContents();
        for (int j = 0; j < mcItems.size(); j++) {
            items[i + j] = MagmaItemStack.asCraftMirror(mcItems.get(i));
        }

        return items;
    }

    @Override
    public void setContents(ItemStack[] items) throws IllegalArgumentException {
        if (getSize() > items.length) {
            throw new IllegalArgumentException(
                "Invalid inventory size; expected " + getSize() + " or less");
        }
        setContents(items[0], Arrays.copyOfRange(items, 1, items.length));
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        setResult(result);
        setMatrix(contents);
    }

    @Override
    public ItemStack getItem(int index) {
        if (index < getResultInventory().getSizeInventory()) {
            net.minecraft.item.ItemStack item = getResultInventory().getStackInSlot(index);
            return item.isEmpty() ? null : MagmaItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = getMatrixInventory()
                .getStackInSlot(index - getResultInventory().getSizeInventory());
            return item.isEmpty() ? null : MagmaItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getResultInventory().getSizeInventory()) {
            getResultInventory().setInventorySlotContents(index, MagmaItemStack.asNMSCopy(item));
        } else {
            getMatrixInventory()
                .setInventorySlotContents(index - getResultInventory().getSizeInventory(),
                    MagmaItemStack.asNMSCopy(item));
        }
    }

    @Override
    public ItemStack getResult() {
        net.minecraft.item.ItemStack item = getResultInventory().getStackInSlot(0);
        if (!item.isEmpty()) {
            return MagmaItemStack.asCraftMirror(item);
        }
        return null;
    }

    @Override
    public void setResult(ItemStack newResult) {
        List<net.minecraft.item.ItemStack> contents = ((IBridgeIInventory) getResultInventory())
            .getContents();
        contents.set(0, MagmaItemStack.asNMSCopy(newResult));
    }

    @Override
    public ItemStack[] getMatrix() {
        List<net.minecraft.item.ItemStack> matrix = ((IBridgeIInventory) getMatrixInventory())
            .getContents();

        return asCraftMirror(matrix);
    }

    @Override
    public void setMatrix(ItemStack[] contents) {
        if (getMatrixInventory().getSizeInventory() > contents.length) {
            throw new IllegalArgumentException(
                "Invalid inventory size; expected " + getMatrixInventory().getSizeInventory()
                    + " or less");
        }

        for (int i = 0; i < getMatrixInventory().getSizeInventory(); i++) {
            if (i < contents.length) {
                getMatrixInventory()
                    .setInventorySlotContents(i, MagmaItemStack.asNMSCopy(contents[i]));
            } else {
                getMatrixInventory()
                    .setInventorySlotContents(i, net.minecraft.item.ItemStack.EMPTY);
            }
        }
    }

    @Override
    public Recipe getRecipe() {
        IRecipe recipe = ((IBridgeIInventory) getInventory()).getCurrentRecipe();
        return recipe == null ? null : ((IBridgeIRecipe) recipe).toBukkitRecipe();
    }
}
