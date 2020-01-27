package org.magmafoundation.magma.mixin.core.minecraft.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateHolder;
import net.minecraftforge.registries.ForgeRegistries;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MixinStateHolder
 *
 * @author Redned
 * @since 26/01/2019 - 12:41 pm
 */
@Mixin(StateHolder.class)
public abstract class MixinStateHolder<O, S> {

    @Shadow @Final private static Function<Map.Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING;
    @Shadow public abstract boolean shadow$equals(Object p_equals_1_);

    public String blockStateToString(BlockState blockState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).toString());
        if (!blockState.getValues().isEmpty()) {
            stringBuilder.append('[');
            stringBuilder.append(blockState.getValues().entrySet().stream().map(MAP_ENTRY_TO_STRING).collect(Collectors.joining(",")));
            stringBuilder.append(']');
        }
        return stringBuilder.toString();
    }
}
