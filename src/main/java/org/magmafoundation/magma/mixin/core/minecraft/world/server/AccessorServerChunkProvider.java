package org.magmafoundation.magma.mixin.core.minecraft.world.server;

import net.minecraft.world.server.ServerChunkProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * AccessorChunkProvider
 *
 * @author Redned
 * @since 26/01/2020 - 06:01 pm
 */
@Mixin(ServerChunkProvider.class)
public interface AccessorServerChunkProvider {

    @Accessor("spawnHostiles") boolean accessor$getSpawnHostiles();
    @Accessor("spawnHostiles") void accessor$setSpawnHostiles(boolean spawnHostiles);

    @Accessor("spawnPassives") boolean accessor$getSpawnPassives();
    @Accessor("spawnPassives") void accessor$setSpawnPassives(boolean spawnPassives);
}
