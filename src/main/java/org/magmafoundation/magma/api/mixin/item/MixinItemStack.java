package org.magmafoundation.magma.api.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IRegistryDelegate;
import org.magmafoundation.magma.api.bridge.item.IBridgeItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinItemStack
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 26/11/2019 - 09:17 am
 */
@Mixin(ItemStack.class)
public abstract class MixinItemStack implements IBridgeItemStack {

    @Mutable
    @Shadow
    @Final
    @Deprecated
    private Item item;

    @Shadow
    private IRegistryDelegate<Item> delegate;

    @Shadow
    public abstract void setDamage(int damage);

    @Shadow
    public abstract int getDamage();

    @Override
    public void setItem(Item item) {
        this.item = item;
        this.delegate = item.delegate;
        this.setDamage(getDamage());
    }
}
