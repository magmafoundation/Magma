package org.magmafoundation.magma.api.mixin.inventory.container;

import com.google.common.base.Preconditions;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.inventory.InventoryView;
import org.magmafoundation.magma.api.bridge.inventory.IBridgeCraftingInventorty;
import org.magmafoundation.magma.api.bridge.inventory.IBridgeIInventory;
import org.magmafoundation.magma.api.bridge.inventory.container.IBridgeContainer;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;
import org.magmafoundation.magma.api.core.inventory.MagmaInventory;
import org.spongepowered.asm.mixin.Mixin;

/**
 * MixinContainer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 06:40 am
 */
@Mixin(Container.class)
public abstract class MixinContainer implements IBridgeContainer {

    public boolean checkReachable = true;
    private ITextComponent title;


    @Override
    public boolean getCheckReachable() {
        return checkReachable;
    }

    @Override
    public void setCheckReachable(boolean reachable) {
        this.checkReachable = reachable;
    }

    @Override
    public ITextComponent getTitle() {
        Preconditions.checkState(this.title != null, "Title not set");
        return this.title;
    }

    @Override
    public void setTitle(ITextComponent title) {
        Preconditions.checkState(this.title == null, "Title already set");
        this.title = title;
    }

    @Override
    public void transferTo(Container other, MagmaHumanEntity player) {
        InventoryView source = this.getBukkitView(), destination = ((IBridgeContainer) other)
            .getBukkitView();
        ((IBridgeIInventory) ((MagmaInventory) source.getTopInventory()).getInventory())
            .onClose(player);
        ((IBridgeCraftingInventorty) ((MagmaInventory) source.getBottomInventory()).getInventory())
            .onClose(player);
        ((IBridgeIInventory) ((MagmaInventory) destination.getTopInventory()).getInventory())
            .onOpen(player);
        ((IBridgeIInventory) ((MagmaInventory) destination.getBottomInventory()).getInventory())
            .onOpen(player);
    }
}
