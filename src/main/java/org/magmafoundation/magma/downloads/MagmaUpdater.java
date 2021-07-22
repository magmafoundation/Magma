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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.magmafoundation.magma.Magma;

/**
 * MagmaUpdater
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 09/05/2020 - 03:51 pm
 */
public class MagmaUpdater {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String newSha;
    private String currentSha;

    public boolean versionChecker() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.magmafoundation.org/api/resources/magma/1.12/dev/latest").openStream()))) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            Date created_at = Date.from(Instant.parse(root.get("created_at").getAsString()));
            String date = new SimpleDateFormat("dd-MM-yyyy").format(created_at);
            String time = new SimpleDateFormat("H:mm a").format(created_at);

            newSha = root.get("tag_name").toString().substring(2, 9);
            currentSha = Magma.class.getPackage().getImplementationVersion().split("-")[0];

            if (currentSha.equals(newSha)) {
                System.out.printf("No update found, latest version: (%s) current version: (%s)%n", currentSha, newSha);
                return false;
            } else {
                System.out.printf("The latest Magma version is (%s) but you have (%s). The latest version was built on %s at %s.%n", newSha, currentSha, date, time);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void downloadJar() {
        String url = "https://api.magmafoundation.org/api/resources/magma/1.12/dev/v"+newSha+"/download";
        try {
            Path path = Paths.get(MagmaUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            System.out.println("Updating Magma Jar ...");
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(path.toFile());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Failed to download update! Starting old version.");
            return;
        }
        System.out.println("Download Complete! Please restart the server.");
        System.exit(0);
    }

}
