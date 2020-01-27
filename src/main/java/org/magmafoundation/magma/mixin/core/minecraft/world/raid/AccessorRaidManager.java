package org.magmafoundation.magma.mixin.core.minecraft.world.raid;

import net.minecraft.world.raid.Raid;
import net.minecraft.world.raid.RaidManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/**
 * AccessorChunkProvider
 *
 * @author Redned
 * @since 26/01/2020 - 06:17 pm
 */
@Mixin(RaidManager.class)
public interface AccessorRaidManager {

    @Accessor("byId") Map<Integer, Raid> accessor$getActiveRaids();
    @Accessor("byId") void accessor$setActiveRaids(Map<Integer, Raid> raids);
}
