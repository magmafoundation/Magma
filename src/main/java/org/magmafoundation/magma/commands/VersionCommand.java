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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.magmafoundation.magma.Magma;

/**
 * VersionCommand
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 04:57 am
 */
public class VersionCommand extends Command {

    public VersionCommand(String name) {
        super(name);

        this.description = "Gets the version of the server";
        this.usageMessage = "/version";
        this.setAliases(Collections.singletonList("ver"));
        this.setPermission("bukkit.command.version");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        sender.sendMessage(
            "This server is running " + Bukkit.getName() + " version " + Magma.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ", Forge Version " + ForgeVersion.getVersion()
                + ")");
        return true;
    }
}
