package org.magmafoundation.magma.commands;

import java.util.Arrays;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.magmafoundation.magma.utils.TPSTracker;

/**
 * TPSCommand
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 12/02/2020 - 06:46 am
 */
public class TPSCommand extends Command {

    public TPSCommand(String name) {
        super(name);
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission("spigot.command.tps");
    }

    public static String format(double tps)  // Paper - Made static
    {
        return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
            + ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }

    private static final long mean(long[] array) {
        if(array == null) return -1;
        long r = 0;
        for (long i : array) {
            r += i;
        }
        return r / array.length;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        World currentWorld = null;
        if (sender instanceof CraftPlayer) {
            currentWorld = ((CraftPlayer) sender).getWorld();
        }
        sender.sendMessage(String
            .format("%s%s%s-----------%s%s%s<%s%s Worlds %s%s%s>%s%s%s-----------", ChatColor.GRAY, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.DARK_GRAY, ChatColor.BOLD,
                ChatColor.STRIKETHROUGH, ChatColor.GREEN, ChatColor.ITALIC, ChatColor.DARK_GRAY, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.GRAY, ChatColor.BOLD, ChatColor.STRIKETHROUGH));
        final MinecraftServer server = MinecraftServer.getServerInst();
        ChatColor colourTPS;
        for (World world : server.server.getWorlds()) {
            if (world instanceof CraftWorld) {
                boolean current = currentWorld != null && currentWorld == world;
                net.minecraft.world.WorldServer mcWorld = ((CraftWorld) world).getHandle();
                String bukkitName = world.getName();
                int dimensionId = mcWorld.provider.getDimension();
                String name = mcWorld.provider.getDimensionType().getName();
                String displayName = name.equals(bukkitName) ? name : String.format("%s | %s", name, bukkitName);

                double worldTickTime = mean(server.worldTickTimes.get(dimensionId)) * 1.0E-6D;
                double worldTPS = Math.min(1000.0 / worldTickTime, 20);

                if (worldTPS >= 18.0) {
                    colourTPS = ChatColor.GREEN;
                } else if (worldTPS >= 15.0) {
                    colourTPS = ChatColor.YELLOW;
                } else {
                    colourTPS = ChatColor.RED;
                }

                sender.sendMessage(String.format("%s[%d] %s%s %s- %s%.2fms / %s%.2ftps", ChatColor.GOLD, dimensionId,
                    current ? ChatColor.GREEN : ChatColor.YELLOW, displayName, ChatColor.RESET,
                    ChatColor.YELLOW, worldTickTime, colourTPS, worldTPS));
            }
        }

        double meanTickTime = mean(server.tickTimeArray) * 1.0E-6D;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);
        if (meanTPS >= 18.0) {
            colourTPS = ChatColor.GREEN;
        } else if (meanTPS >= 15.0) {
            colourTPS = ChatColor.YELLOW;
        } else {
            colourTPS = ChatColor.RED;
        }
        sender.sendMessage(String.format("%s%sOverall: %s%s%.2fms / %s%.2ftps", ChatColor.WHITE, ChatColor.BOLD, ChatColor.RESET,
            ChatColor.YELLOW, meanTickTime, colourTPS, meanTPS));
        // Paper start - Further improve tick handling
        double[] tps = org.bukkit.Bukkit.getTPS();
        String[] tpsAvg = Arrays.stream(tps).mapToObj(TPSCommand::format).toArray(String[]::new);

        sender.sendMessage(String
            .format("%s%s%s-----------%s%s%s<%s%s TPS Graph (48 Seconds) %s%s%s>%s%s%s-----------", ChatColor.GRAY, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.DARK_GRAY, ChatColor.BOLD,
                ChatColor.STRIKETHROUGH, ChatColor.GREEN, ChatColor.ITALIC, ChatColor.DARK_GRAY, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.GRAY, ChatColor.BOLD, ChatColor.STRIKETHROUGH));
        if (!TPSTracker.lines.isEmpty()) {
            TPSTracker.lines.forEach(sender::sendMessage);
        }
        String status = ChatColor.GRAY + "Unknown";
        try {
            final double currentTPS = (double) Double.valueOf(DedicatedServer.TPS);
            if (currentTPS >= 17.0) {
                status = ChatColor.GREEN + "STABLE";
            } else if (currentTPS >= 15.0) {
                status = ChatColor.YELLOW + "SOME STABILITY ISSUES";
            } else if (currentTPS >= 10.0) {
                status = ChatColor.RED + "LAGGING. CHECK TIMINGS.";
            } else if (currentTPS < 10.0) {
                status = ChatColor.DARK_RED + "UNSTABLE";
            } else if (currentTPS < 3.0) {
                status = ChatColor.RED + "SEND HELP!!!!!";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sender.sendMessage(String.format("%s%sServer Status: %s", ChatColor.WHITE, ChatColor.BOLD, status));
        return true;
    }


}
