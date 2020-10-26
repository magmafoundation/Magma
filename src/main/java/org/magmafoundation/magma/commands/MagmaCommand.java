package org.magmafoundation.magma.commands;

import java.util.Collections;
import java.util.List;
import net.minecraft.command.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.magmafoundation.magma.api.PlayerAPI;
import org.magmafoundation.magma.api.ServerAPI;

/**
 * MagmaCommand
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 04:40 pm
 */
public class MagmaCommand extends Command {

    public MagmaCommand(String name) {
        super(name);

        this.description = "Magma commands";
        this.usageMessage = "/magma [mods|playermods]";
        this.setPermission("magma.commands.magma");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length <= 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "mods", "playermods");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!sender.hasPermission("magma.commands.magma")) {
            sender.sendMessage(ChatColor.RED + "You don't got the permission to execute this command!");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "mods":
                sender.sendMessage(ChatColor.GREEN + "" + ServerAPI.getModSize() + " " + ServerAPI.getModList());
                break;
            case "playermods":
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                    return false;
                }

                Player player = Bukkit.getPlayer(args[1].toString());
                if (player != null) {
                    sender.sendMessage(ChatColor.GREEN + "" + PlayerAPI.getModSize(player) + " " + PlayerAPI.getModlist(player));
                } else {
                    sender.sendMessage(ChatColor.RED + "The player [" + args[1] + "] is not online.");
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }
}
