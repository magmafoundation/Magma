package org.magmafoundation.magma.mixin.bridge.minecraft.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

import org.magmafoundation.magma.bridge.minecraft.block.state.BridgeBlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(BlockState.class)
public class MixinBlockState implements BridgeBlockState {

    private Map<IProperty<?>, Comparable<?>> parsedProperties;

    @Override
    public Map<IProperty<?>, Comparable<?>> bridge$getParsedProperties() {
        return parsedProperties;
    }

    @Override
    public void bridge$setParsedProperties(Map<IProperty<?>, Comparable<?>> parsedProperties) {
        this.parsedProperties = parsedProperties;
    }
}
