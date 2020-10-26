package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class AsyncCatcher {

    public static boolean enabled = true;
    public static boolean shuttingDown = false; // Paper

    public static void catchOp(String reason) {
        if (enabled && Thread.currentThread() != MinecraftServer.getServerInstance().primaryThread) {
            throw new IllegalStateException("Asynchronous " + reason + "!");
        }
    }

    public static boolean catchInv() {
        if (enabled && Thread.currentThread() != MinecraftServer.getServerInstance().primaryThread) {
            return true;
        }
        return false;
    }
}
