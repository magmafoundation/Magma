package org.magmafoundation.magma.api.mixin.inventory;

import net.minecraft.inventory.IInventory;
import org.magmafoundation.magma.api.bridge.inventory.IBridgeIInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * IMagmaInventory
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 26/11/2019 - 07:23 am
 */
@Mixin(IInventory.class)
public interface MixinMagmaIInventory extends IBridgeIInventory {

    /**
     * @author
     */
    @Overwrite
    default int getInventoryStackLimit() {
        return 0;
    }
}
