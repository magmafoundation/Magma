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

    /**
     * Downloads the required libraries zip from the git repo and extracts the libraries zip into the correct place.
     */
    public static void downloadServerLibraries() {
        String fileName = "libraries.zip";
        String downloadLink = "https://raw.githubusercontent.com/MagmaFoundation/Magma/master/release/libraries.zip";

        File minecraftlibraries = new File(fileName);
        if (!minecraftlibraries.exists() && !minecraftlibraries.isDirectory()
            || getLibrariesVersion()) {
            System.out.println("Downloading Server Libraries ...");
            try {
                URL website = new URL(downloadLink);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                System.out.println("Extracting Zip");
                unzip(minecraftlibraries, "./");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Checks if the current libraries are up to date with the project if they are not they will re-download latest versions.
     *
     * @return True/False depending on whether the version are the same
     */
    public static boolean getLibrariesVersion() {
        String s = Magma.getLibraryVersion();
        File lib = new File("./libraries.version");
        if (!lib.exists()) {
            return true;
        }

        String i = MagmaConfig.getString(lib, "version:", Magma.getLibraryVersion());
        return !i.equals(s);
    }


    /**
     * Extract files and folders in a zip file.
     *
     * @param source Zip file to extract data from.
     * @param out Folder location to put the extracted data into.
     * @throws IOException if there's an error extracting
     */
    private static void unzip(File source, String out) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                File file = new File(out, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] buffer = new byte[Math.toIntExact(entry.getSize())];
                        int location;
                        while ((location = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, location);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        }
    }
}
