package org.magmafoundation.magma.bridge.minecraft.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

import java.util.Map;

/**
 * BridgeBlockState
 *
 * @author Redned
 * @since 26/01/2020 - 12:34 pm
 */
public interface BridgeBlockState {

    Map<IProperty<?>, Comparable<?>> bridge$getParsedProperties();

    void bridge$setParsedProperties(Map<IProperty<?>, Comparable<?>> parsedStates);
}
