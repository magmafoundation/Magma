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

package org.magmafoundation.magma.downloads;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.craftbukkit.v1_12_R1.Main;
import org.magmafoundation.magma.Magma;
import org.magmafoundation.magma.configuration.MagmaConfig;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * DownloadServerFiles
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 02/05/2019 - 06:06 PM
 */
public class DownloadServerFiles {

    /**
     * Downloads required Minecraft server jar from the Minecraft cdn.
     */
    public static void downloadMinecraftServer() {
        String fileName = "minecraft_server.1.12.2.jar";
        String downloadLink = "https://launcher.mojang.com/v1/objects/886945bfb2b978778c3a0288fd7fab09d315b25f/server.jar";

        File minecraftServerJar = new File(fileName);
        if (!minecraftServerJar.exists() && !minecraftServerJar.isDirectory()) {
            System.out.println("Downloading Minecraft Server Jar ...");
            try {
                URL website = new URL(downloadLink);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
