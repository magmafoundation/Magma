package org.magmafoundation.magma.mixin.core.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.magmafoundation.magma.bridge.minecraft.block.state.BridgeBlockState;
import org.magmafoundation.magma.mixin.core.minecraft.block.state.MixinStateHolder;
import org.spongepowered.asm.mixin.*;

/**
 * MixinBlockState
 *
 * @author Redned
 * @since 26/01/2019 - 12:34 pm
 */
@Mixin(BlockState.class)
public abstract class MixinBlockState extends MixinStateHolder<Block, BlockState> implements BlockData {

    @Shadow public abstract Block getBlock();

    @NotNull
    @Override
    public Material getMaterial() {
        // TODO: Modded materials
        return Material.matchMaterial(getBlock().getRegistryName().getPath()); // TODO
    }

    @NotNull
    @Override
    public String getAsString() {
        return blockStateToString((BlockState) (Object) this);
    }

    @NotNull
    @Override
    public String getAsString(boolean hideUnspecified) {
        return getAsString(); // TODO
    }

    @NotNull
    @Override
    public BlockData merge(@NotNull BlockData data) {
        BlockState state = (BlockState) data;
        BlockState cloned = (BlockState) this.clone();

        BridgeBlockState stateBridge = (BridgeBlockState) state;
        ((BridgeBlockState) cloned).bridge$setParsedProperties(null);

        for (IProperty parsed : stateBridge.bridge$getParsedProperties().keySet()) {
            cloned = cloned.with(parsed, state.get(parsed));
        }

        return (BlockData) cloned;
    }

    @Override
    public boolean matches(@Nullable BlockData data) {
        if (data == null)
            return false;

        BlockState state = (BlockState) data;
        if (!this.getBlock().equals(state.getBlock()))
            return false;

        boolean exact = this.equals(data);
        if (!exact && ((BridgeBlockState) state).bridge$getParsedProperties() != null) {
            return this.merge(data).equals(this);
        }

        return exact;
    }

    @NotNull
    @Override
    public BlockData clone() {
        try {
            return (BlockData) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError("Clone not supported", ex);
        }
    }
}
