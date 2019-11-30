package org.magmafoundation.magma.api.bridge.inventory.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.inventory.InventoryView;
import org.magmafoundation.magma.api.core.entity.MagmaHumanEntity;

/**
 * IBridgeContainer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 06:56 am
 */
public interface IBridgeContainer {

    InventoryView getBukkitView();

    boolean getCheckReachable();

    void setCheckReachable(boolean reachable);

    ITextComponent getTitle();

    void setTitle(ITextComponent title);

    void transferTo(Container other, MagmaHumanEntity player);

}
