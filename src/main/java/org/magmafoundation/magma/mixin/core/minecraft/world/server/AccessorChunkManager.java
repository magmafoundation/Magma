package org.magmafoundation.magma.mixin.core.minecraft.world.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;

import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * AccessorChunkManager
 *
 * @author Redned
 * @since 26/01/2020 - 05:28 pm
 */
@Mixin(ChunkManager.class)
public interface AccessorChunkManager {

    @Accessor("field_219252_f") Long2ObjectLinkedOpenHashMap<ChunkHolder> accessor$getVisibleChunks();
}
