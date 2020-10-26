package org.bukkit.craftbukkit.v1_12_R1.chunkio;

import co.aikar.timings.Timing;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.bukkit.craftbukkit.v1_12_R1.util.AsynchronousExecutor;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider<QueuedChunk, Chunk, Runnable, RuntimeException> {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    // async stuff
    public Chunk callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        try (Timing ignored = queuedChunk.provider.world.timings.chunkIOStage1.startTimingIfSync()) { // Paper
            AnvilChunkLoader loader = queuedChunk.loader;
            Object[] data = loader.loadChunk__Async(queuedChunk.world, queuedChunk.x, queuedChunk.z);
            
            if (data != null) {
                queuedChunk.compound = (NBTTagCompound) data[1];
                return (Chunk) data[0];
            }

            return null;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
            // Paper - Mirror vanilla by catching everything (else) rather than immediately crashing the server
            // stage2 will receive a null chunk and then load it synchronously, where vanilla MC will properly log and recover
            // stage2 will _not_ however return that instance, only load it
        } catch (Exception ex) {
            return null;
        }
    }

    // sync stuff
    public void callStage2(QueuedChunk queuedChunk, Chunk chunk) throws RuntimeException {
        if (chunk == null || queuedChunk.provider.id2ChunkMap.containsKey(ChunkPos.asLong(queuedChunk.x, queuedChunk.z))) { // Paper - also call original if it was already loaded
            // If the chunk loading failed (or was already loaded for some reason) just do it synchronously (may generate)
            queuedChunk.provider.provideChunk(queuedChunk.x, queuedChunk.z);
            return;
        }

        try (Timing ignored = queuedChunk.provider.world.timings.chunkIOStage2.startTimingIfSync()) { // Paper
        queuedChunk.loader.loadEntities(queuedChunk.world, queuedChunk.compound.getCompoundTag("Level"), chunk);
            chunk.setLastSaveTime(queuedChunk.provider.world.getTotalWorldTime());
            queuedChunk.provider.id2ChunkMap.put(ChunkPos.asLong(queuedChunk.x, queuedChunk.z), chunk);
            chunk.onLoad();

            if (queuedChunk.provider.chunkGenerator != null) {
                queuedChunk.provider.chunkGenerator.recreateStructures(chunk, queuedChunk.x, queuedChunk.z);
            }

            chunk.populateCB(queuedChunk.provider, queuedChunk.provider.chunkGenerator, false);
        } // Paper
    }

    public void callStage3(QueuedChunk queuedChunk, Chunk chunk, Runnable runnable) throws RuntimeException {
        runnable.run();
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Chunk I/O Executor Thread-" + threadNumber.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
