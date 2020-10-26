package com.destroystokyo.paper;

import co.aikar.timings.Timing;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.ArrayDeque;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class PaperLightingQueue {
    private static final long MAX_TIME = (long) (1000000000 / 20 * .95);
    private static int updatesThisTick;
    public static void processQueue(long curTime) {
        updatesThisTick = 0;
        final long startTime = System.nanoTime();
        final long maxTickTime = MAX_TIME - (startTime - curTime);
        START:
        for (World world : MinecraftServer.getServerInstance().worlds) {
            if (!world.paperConfig.queueLightUpdates) {
                continue;
            }
            ObjectCollection<Chunk> loadedChunks = ((WorldServer) world).getChunkProvider().id2ChunkMap.values();
            for (Chunk chunk : loadedChunks.toArray(new Chunk[loadedChunks.size()])) {
                if (chunk.lightingQueue.processQueue(startTime, maxTickTime)) {
                    break START;
                }
            }
        }
    }
    public static class LightingQueue extends ArrayDeque<Runnable> {
        final private Chunk chunk;
        public LightingQueue(Chunk chunk) {
            super();
            this.chunk = chunk;
        }
        /**
         * Processes the lighting queue for this chunk
         *
         * @param startTime If start Time is 0, we will not limit execution time
         * @param maxTickTime Maximum time to spend processing lighting updates
         * @return true to abort processing furthur lighting updates
         */
        private boolean processQueue(long startTime, long maxTickTime) {
            if (this.isEmpty()) {
                return false;
            }
            try (Timing ignored = chunk.world.timings.lightingQueueTimer.startTiming()) {
                Runnable lightUpdate;
                while ((lightUpdate = this.poll()) != null) {
                    lightUpdate.run();
                    if (startTime > 0 && ++PaperLightingQueue.updatesThisTick % 10 == 0 && PaperLightingQueue.updatesThisTick > 10) {
                        if (System.nanoTime() - startTime > maxTickTime) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        /**
         * Flushes lighting updates to unload the chunk
         */
        public void processUnload() {
            if (!chunk.world.paperConfig.queueLightUpdates) {
                return;
            }
            processQueue(0, 0); // No timeout
            final int radius = 1; // TODO: bitflip, why should this ever be 2?
            for (int x = chunk.x - radius; x <= chunk.z + radius; ++x) {
                for (int z = chunk.z - radius; z <= chunk.z + radius; ++z) {
                    if (x == chunk.x && z == chunk.z) {
                        continue;
                    }
                    Chunk neighbor = MCUtil.getLoadedChunkWithoutMarkingActive(chunk.world, x, z);
                    if (neighbor != null) {
                        neighbor.lightingQueue.processQueue(0, 0); // No timeout
                    }
                }
            }
        }
    }
}
