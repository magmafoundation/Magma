/*
 * Magma Server
 * Copyright (C) 2019-2020.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.magmafoundation.magma.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.magmafoundation.magma.api.PlayerAPI;
import org.magmafoundation.magma.api.ServerAPI;

import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

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
        this.usageMessage = "/magma [mods|playermods|dump]";
        this.setPermission("magma.commands.magma");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length <= 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "mods", "playermods", "dump");
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
        case "dump":
            createMagmaDump("worlds.mdump");
            createMagmaDump("permissions.mdump");
            createMagmaDump("materials.mdump");
            createMagmaDump("biomes.mdump");
            sender.sendMessage(ChatColor.RED + "Dump saved!");
            break;
        default:
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        return true;
    }

    private void createMagmaDump(String fileName) {
        try {


            File dumpFolder = new File("dump");
            dumpFolder.mkdirs();
            File dumpFile = new File(dumpFolder, fileName);
            OutputStream os = new FileOutputStream(dumpFile);
            Writer writer = new OutputStreamWriter(os);

            switch (fileName.split("\\.")[0]) {
            case "worlds":
                for (WorldServer world : DimensionManager.getWorlds()) {
                    writer.write(String.format("Stats for %s [%s] with id %d\n", world, world.provider.getDimensionType().name(), world.dimension));
                    writer.write("Current Tick: " + world.worldInfo.getWorldTotalTime() + "\n");
                    writer.write("\nEntities: ");
                    writer.write("count - " + world.loadedEntityList.size() + "\n");
                    for (Entity entity : world.loadedEntityList) {
                        writer.write(String.format(" %s at (%.4f,%.4f,%.4f)\n", entity.getClass().getName(), entity.posX, entity.posY, entity.posZ));
                    }
                    writer.write("\nTileEntities: ");
                    writer.write("count - " + world.loadedTileEntityList.size() + "\n");
                    for (TileEntity entity : world.loadedTileEntityList) {
                        writer.write(String.format(" %s at (%d,%d,%d)\n", entity.getClass().getName(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()));
                    }
                    writer.write("\nLoaded Chunks: ");
                    writer.write("count - " + world.getChunkProvider().getLoadedChunkCount() + "\n");
                    writer.write("------------------------------------\n");
                }
                writer.close();
            case "permissions":

                for (Command command : MinecraftServer.getServerInstance().server.getCommandMap().getCommands()) {
                    if (command.getPermission() == null) {
                        continue;
                    }
                    writer.write(command.getName() + ": " + command.getPermission() + "\n");
                }

                writer.close();
            case "materials":

                for (Material material : Material.values()) {
                    writer.write(material.name() + "\n");
                }

                writer.close();
            case "biomes":

                for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
                    writer.write(biome.getRegistryName() + "\n");
                }

                writer.close();
            }
        } catch (Exception e) {

        }


    }
}
