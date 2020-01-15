package org.magmafoundation.magma.api.mixin.inventory;

import static org.magmafoundation.magma.api.bridge.inventory.IBridgeIInventory.MAX_STACK;

import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.magmafoundation.magma.api.bridge.entity.IBridgeEntity;
import org.magmafoundation.magma.api.bridge.inventory.IBridgeCraftingInventorty;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinCraftingInventory
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 27/11/2019 - 07:05 am
 */
@Mixin(CraftingInventory.class)
public class MixinCraftingInventory implements IBridgeCraftingInventorty {

    //@formatter:off
    @Mutable @Shadow @Final private NonNullList<ItemStack> stackList;
    @Mutable @Shadow @Final private Container field_70465_c;
    @Mutable @Shadow @Final private int width;
    @Mutable @Shadow @Final private int height;
    //@formatter:on

    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private IRecipe currentRecipe;
    public IInventory resultInventory;
    private PlayerEntity owner;
    private int maxStack = MAX_STACK;


    @Override
    public void onOpen(MagmaHumanEntity who) {
        transaction.add(who);
    }

    @Override
    public void onClose(MagmaHumanEntity who) {
        transaction.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public List<ItemStack> getContents() {
        return stackList;
    }

    @Override
    public InventoryType getInventoryType() {
        return stackList.size() == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
    }

    @Override
    public InventoryHolder getOwner() {
        return (owner == null) ? null : (InventoryHolder) ((IBridgeEntity) owner).getBukkitEntity();
    }

    @Override
    public void setOwner(PlayerEntity playerEntity) {
        owner = playerEntity;
    }

    @Override
    public IRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    @Override
    public void setCurrentRecipe(IRecipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    @Override
    public IInventory getResultInventory() {
        return resultInventory;
    }

    @Override
    public void setResultInventory(IInventory resultInventory) {
        this.resultInventory = resultInventory;
    }

    @Override
    public int getMaxStack() {
        return maxStack;
    }

    @Override
    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }
}
