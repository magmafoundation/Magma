package org.magmafoundation.magma.api.mixin.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.PlayerContainer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.magmafoundation.magma.api.bridge.entity.IBridgeEntity;
import org.magmafoundation.magma.api.bridge.entity.IBridgePlayerEntity;
import org.magmafoundation.magma.api.bridge.inventory.IBridgeCraftingInventorty;
import org.magmafoundation.magma.api.bridge.inventory.container.IBridgePlayerContainer;
import org.magmafoundation.magma.api.core.inventory.MagmaInventoryCrafting;
import org.magmafoundation.magma.api.core.inventory.MagmaInventoryView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MixinPlayerContainer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 27/11/2019 - 06:52 am
 */
@Mixin(PlayerContainer.class)
public class MixinPlayerContainer extends MixinContainer implements IBridgePlayerContainer {

    private MagmaInventoryView bukkitEntity = null;
    private PlayerInventory playerInventory;

    //@formatter:off
    @Mutable@Shadow @Final private CraftingInventory field_75181_e;
    @Mutable @Shadow @Final private CraftResultInventory field_75179_f;
    //@formatter:on

    @Shadow
    @Final
    private PlayerEntity player;

    @Inject(method = "<init>", at = @At("HEAD"))
    private void init(PlayerInventory playerInventory, boolean localWorld, PlayerEntity playerIn,
        CallbackInfo ci) {
        field_75179_f = new CraftResultInventory();
        field_75181_e = new CraftingInventory((PlayerContainer) (Object) this, 2, 2);
        ((IBridgeCraftingInventorty) field_75181_e).setOwner(playerInventory.player);
        ((IBridgeCraftingInventorty) field_75181_e).setResultInventory(field_75179_f);
        this.playerInventory = playerInventory;
    }


    @Override
    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    @Override
    public InventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        MagmaInventoryCrafting inventoryCrafting = new MagmaInventoryCrafting(this.field_75181_e,
            this.field_75179_f);
        bukkitEntity = new MagmaInventoryView((PlayerContainer) (Object) this,
             ((IBridgePlayerEntity) this.playerInventory.player).getBukkitEntity(),
            inventoryCrafting
        );

        return bukkitEntity;
    }
}
