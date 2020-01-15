package org.magmafoundation.magma.api.core.inventory;

import net.minecraft.inventory.container.Container;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;

/**
 * MagmaInventoryView
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 07:11 am
 */
public class MagmaInventoryView extends InventoryView {

    private final Container container;
    private final MagmaHumanEntity player;
    private final MagmaInventory viweing;

    public MagmaInventoryView(Container container, HumanEntity player, Inventory viweing) {
        this.container = container;
        this.player = (MagmaHumanEntity) player;
        this.viweing = (MagmaInventory) viweing;
    }

    @Override
    public Inventory getTopInventory() {
        return viweing;
    }

    @Override
    public Inventory getBottomInventory() {
        return player.getInventory();
    }

    @Override
    public HumanEntity getPlayer() {
        return player;
    }

    @Override
    public InventoryType getType() {
        InventoryType type = viweing.getType();
        if (type == InventoryType.CRAFTING && player.getGameMode() == GameMode.CREATIVE) {
            return InventoryType.CREATIVE;
        }
        return type;
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        net.minecraft.item.ItemStack stack = MagmaItemStack.asNMSCopy(item);
        if (slot >= 0) {
            container.getSlot(slot).putStack(stack);
        } else {
            player.getHandle().dropItem(stack, false);
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0) {
            return null;
        }
        return MagmaItemStack.asCraftMirror(container.getSlot(slot).getStack());
    }

    public boolean isInTop(int rawSlot) {
        return rawSlot < viweing.getSize();
    }

    public Container getHandle() {
        return container;
    }
}
