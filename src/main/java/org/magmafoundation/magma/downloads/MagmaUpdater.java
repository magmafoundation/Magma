package org.magmafoundation.magma.downloads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.magmafoundation.magma.Magma;

/**
 * MagmaUpdater
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 09/05/2020 - 03:51 pm
 */
public class MagmaUpdater {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String time;
    private String newSha;
    private String currentSha;

    public boolean versionChecker() {
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new URL("https://ci.hexeption.dev/job/Magma%20Foundation/job/Magma/job/master/lastSuccessfulBuild/api/json").openStream()))) {

            JsonObject root = gson.fromJson(reader, JsonObject.class);
            JsonArray changeSetsItems = changeSetsItems = root.get("changeSets").getAsJsonArray();
            JsonObject changeSet;
            try {
                changeSet = changeSetsItems.get(0).getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Failed to retrieve latest version.");
                return false;
            }

            time = changeSet.get("date").toString().replace("+0100", "").replaceAll("\"", "");
            newSha = changeSet.get("commitId").toString().replaceAll("\"", "").substring(0, 7);
            currentSha = Magma.class.getPackage().getImplementationVersion();

            if (currentSha.equals(newSha)) {
                System.out.println(String.format("No update found, latest version: (%s) current version: (%s)", currentSha, newSha));
                return false;
            } else {
                System.out.println(
                    String.format("The latest Magma version is (%s) but you have (%s). The latest version was built on %s at %s.", newSha, currentSha, time.substring(0, 10), time.substring(11, 19)));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void downloadJar() {
        String url = "https://ci.hexeption.dev/job/Magma%20Foundation/job/Magma/job/master/lastSuccessfulBuild/artifact/build/distributions/Magma-" + newSha + "-server.jar";
        String fileName = MagmaUpdater.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        try {
            System.out.println("Updating Magma Jar ...");
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Download Complete! Please restart the server.");
        System.exit(0);
    }

}
