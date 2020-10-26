package org.spigotmc;

import com.destroystokyo.paper.PaperConfig;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.magmafoundation.magma.Magma;

public class WatchdogThread extends Thread {

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final long earlyWarningEvery; // Paper - Timeout time for just printing a dump but not restarting
    private final long earlyWarningDelay; // Paper
    public static volatile boolean hasStarted; // Paper
    private long lastEarlyWarning; // Paper - Keep track of short dump times to avoid spamming console with short dumps
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart) {
        super("Paper Watchdog Thread");
        this.timeoutTime = timeoutTime;
        this.restart = restart;
        earlyWarningEvery = Math.min(PaperConfig.watchdogPrintEarlyWarningEvery, timeoutTime); // Paper
        earlyWarningDelay = Math.min(PaperConfig.watchdogPrintEarlyWarningDelay, timeoutTime); // Paper
    }

    public static void doStart(int timeoutTime, boolean restart)
    {
        if ( instance == null )
        {
            instance = new WatchdogThread( timeoutTime * 1000L, restart );
            instance.start();
        }
    }

    public static void tick()
    {
        instance.lastTick = System.currentTimeMillis();
    }

    public static void doStop()
    {
        if ( instance != null )
        {
            instance.stopping = true;
        }
    }

    @Override
    public void run()
    {
        while (!stopping) {
            //
            long currentTime = System.currentTimeMillis(); // Paper - do we REALLY need to call this method multiple times?
            if (lastTick != 0 && currentTime > lastTick + earlyWarningEvery && !Boolean.getBoolean("disable.watchdog")) // Paper - Add property to disable and short timeout            {
            {
                // Paper start
                boolean isLongTimeout = currentTime > lastTick + timeoutTime;
                // Don't spam early warning dumps
                if (!isLongTimeout && (earlyWarningEvery <= 0 || !hasStarted || currentTime < lastEarlyWarning + earlyWarningEvery || currentTime < lastTick + earlyWarningDelay)) {
                    continue;
                }
                lastEarlyWarning = currentTime;
                // Paper end
                Logger log = Bukkit.getServer().getLogger();
                // Paper start - Different message when it's a short timeout
                if (isLongTimeout) {
                    log.log(Level.SEVERE, "The server has stopped responding!");
                    log.log(Level.SEVERE, "Please report this to http://github.com/Magmafoundation/Magma/issues");
                    log.log(Level.SEVERE, "Be sure to include ALL relevant console errors and Minecraft crash reports");
                    log.log(Level.SEVERE, "Magma version: " + Magma.getVersion());

                    if (World.haveWeSilencedAPhysicsCrash) {
                        log.log(Level.SEVERE, "------------------------------");
                        log.log(Level.SEVERE, "During the run of the server, a physics stackoverflow was supressed");
                        log.log(Level.SEVERE, "near " + World.blockLocation);
                    }
                    // Paper start - Warn in watchdog if an excessive velocity was ever set
                    if ( org.bukkit.craftbukkit.v1_12_R1.CraftServer.excessiveVelEx != null ) {
                        log.log(Level.SEVERE, "------------------------------");
                        log.log(Level.SEVERE, "During the run of the server, a plugin set an excessive velocity on an entity");
                        log.log(Level.SEVERE, "This may be the cause of the issue, or it may be entirely unrelated");
                        log.log(Level.SEVERE, org.bukkit.craftbukkit.v1_12_R1.CraftServer.excessiveVelEx.getMessage());
                        for (StackTraceElement stack : org.bukkit.craftbukkit.v1_12_R1.CraftServer.excessiveVelEx.getStackTrace()) {
                            log.log(Level.SEVERE, "\t\t" + stack);
                        }
                    }
                    // Paper end
                } else {
                    log.log(Level.SEVERE, "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH ---");
                    log.log(Level.SEVERE, "The server has not responded for " + (currentTime - lastTick) / 1000 + " seconds! Creating thread dump");
                }
                // Paper end - Different message for short timeout
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to Magma!):");
                dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServerInstance().primaryThread.getId(), Integer.MAX_VALUE), log);
                log.log(Level.SEVERE, "------------------------------");
                //
                // Paper start - Only print full dump on long timeouts
                if (isLongTimeout) {
                    log.log(Level.SEVERE, "Entire Thread Dump:");
                    ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                    for (ThreadInfo thread : threads) {
                        dumpThread(thread, log);
                    }
                } else {
                    log.log(Level.SEVERE, "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH ---");
                }

                log.log(Level.SEVERE, "------------------------------");

                if (isLongTimeout) {
                    if (restart) {
                        MinecraftServer.getServerInstance().primaryThread.stop();
                    }
                    break;
                } // Paper end
            }

            try
            {
                sleep(1000); // Paper - Reduce check time to every second instead of every ten seconds, more consistent and allows for short timeout
            } catch ( InterruptedException ex )
            {
                interrupt();
            }
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log)
    {
        log.log( Level.SEVERE, "------------------------------" );
        //
        log.log( Level.SEVERE, "Current Thread: " + thread.getThreadName() );
        log.log( Level.SEVERE, "\tPID: " + thread.getThreadId()
                + " | Suspended: " + thread.isSuspended()
                + " | Native: " + thread.isInNative()
                + " | State: " + thread.getThreadState() );
        if ( thread.getLockedMonitors().length != 0 )
        {
            log.log( Level.SEVERE, "\tThread is waiting on monitor(s):" );
            for ( MonitorInfo monitor : thread.getLockedMonitors() )
            {
                log.log( Level.SEVERE, "\t\tLocked on:" + monitor.getLockedStackFrame() );
            }
        }
        log.log( Level.SEVERE, "\tStack:" );
        //
        for ( StackTraceElement stack : thread.getStackTrace() )
        {
            log.log( Level.SEVERE, "\t\t" + stack );
        }
    }
}
