package org.magmafoundation.magma.downloads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.magmafoundation.magma.utils.JarLoader;

/**
 * LibraryDownloader
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 28/07/2020 - 06:05 pm
 */
public class LibraryDownloader {

    public void run() {
        parseLibraryJson(new InputStreamReader(LibraryDownloader.this.getClass().getResourceAsStream("/magma_libs.json")));
    }

    private void parseLibraryJson(Reader reader) {
        Gson gson = new GsonBuilder().create();

        JsonElement librarys = gson.fromJson(reader, JsonElement.class);
        String verison = librarys.getAsJsonObject().get("version").getAsString();
        JsonArray libs = librarys.getAsJsonObject().get("libraries").getAsJsonArray();
        libs.forEach(jsonElement -> {
            String name = jsonElement.getAsJsonObject().get("name").getAsString();
            String repo = jsonElement.getAsJsonObject().get("repo").getAsString();
            Libary libary = craftLibary(name, repo);
            try {
                File file = downloadFile(libary);
                JarLoader.loadjar(new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader()), file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println();

    }

    private Libary craftLibary(String fullName, String repo) {
        String groupId = fullName.split(":")[0];
        groupId = groupId.replaceAll("\\.", "/"); // Replaces all '.' with '/'
        String artifactId = fullName.split(":")[1];
        String version = fullName.split(":")[2];
        try {
            String time = fullName.split(":")[3]; // Support for Timed versions
            if (time != null) {
                String jarName = artifactId + "-" + version.replace("SNAPSHOT", "") + time + ".jar";
                String folderName = groupId + "/" + artifactId + "/" + version;
                String url = repo + "/" + folderName + "/" + jarName;
                Libary libary = new Libary(jarName, folderName, url);
                System.out.println(libary.toString());
                return libary;
            }
        } catch (Exception e) {
        }

        String jarName = artifactId + "-" + version + ".jar";
        String folderName = groupId + "/" + artifactId + "/" + version;
        String url = repo + "/" + folderName + "/" + jarName;

        Libary libary = new Libary(jarName, folderName, url);
        System.out.println(libary.toString());
        return libary;
    }

    private class Libary {

        private final String jarName;
        private final String folderName;
        private final String url;

        public Libary(String jarName, String folderName, String url) {
            this.jarName = jarName;
            this.folderName = folderName;
            this.url = url;
        }

        @Override
        public String toString() {
            return "Libary{" +
                "jarName='" + jarName + '\'' +
                ", folderName='" + folderName + '\'' +
                ", url='" + url + '\'' +
                '}';
        }
    }

    private File downloadFile(Libary libary) throws IOException {
        URL website = new URL(libary.url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        String folderName = "./libraries/" + libary.folderName + "/";
        new File(folderName).mkdirs();
        FileOutputStream fos = new FileOutputStream(folderName + libary.jarName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return new File(folderName + libary.jarName);
    }
}

