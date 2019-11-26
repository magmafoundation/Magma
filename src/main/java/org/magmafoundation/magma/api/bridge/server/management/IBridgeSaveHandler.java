package org.magmafoundation.magma.api.bridge.server.management;

import net.minecraft.nbt.CompoundNBT;

/**
 * IBridgeSaveHandler
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 07:22 pm
 */
public interface IBridgeSaveHandler {

    CompoundNBT getPlayerNBT(String name);

}
