package org.magmafoundation.magma.downloads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.magmafoundation.magma.utils.JarLoader;
import org.magmafoundation.magma.utils.MD5Checksum;

/**
 * LibraryDownloader
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 28/07/2020 - 06:05 pm
 */
public class LibraryDownloader {

    public void run() {
        System.out.println("Starting Library Downloader");
        JsonArray libs = parseLibraryJson(new InputStreamReader(getClass().getResourceAsStream("/magma_libs.json")));
        libs.forEach(jsonElement -> {
            String name = jsonElement.getAsJsonObject().get("name").getAsString();
            String repo = jsonElement.getAsJsonObject().get("repo").getAsString();
            String md5 = jsonElement.getAsJsonObject().get("md5").getAsString();

            Libary libary = craftLibary(name, repo, md5);
            try {
                File file = downloadFile(libary);
                JarLoader.loadjar(new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader()), file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private JsonArray parseLibraryJson(Reader reader) {
        Gson gson = new GsonBuilder().create();

        JsonElement librarys = gson.fromJson(reader, JsonElement.class);
        String verison = librarys.getAsJsonObject().get("version").getAsString();
        System.out.println("Library Version: " + verison);
        JsonArray libs = librarys.getAsJsonObject().get("libraries").getAsJsonArray();
        return libs;
    }

    private Libary craftLibary(String fullName, String repo, String md5) {
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
                Libary libary = new Libary(jarName, folderName, url, md5);
                return libary;
            }
        } catch (Exception e) {
        }

        String jarName = artifactId + "-" + version + ".jar";
        String folderName = groupId + "/" + artifactId + "/" + version;
        String url = repo + "/" + folderName + "/" + jarName;

        Libary libary = new Libary(jarName, folderName, url, md5);
        return libary;
    }

    private class Libary {

        private final String jarName;
        private final String folderName;
        private final String url;
        private final String md5sum;

        public Libary(String jarName, String folderName, String url, String md5sum) {
            this.jarName = jarName;
            this.folderName = folderName;
            this.url = url;
            this.md5sum = md5sum;
        }

        @Override
        public String toString() {
            return "Libary{" +
                "jarName='" + jarName + '\'' +
                ", folderName='" + folderName + '\'' +
                ", url='" + url + '\'' +
                ", md5sum='" + md5sum + '\'' +
                '}';
        }
    }

    private File downloadFile(Libary libary) throws Exception {
        String folderName = "./libraries/" + libary.folderName + "/";
        String fullPath = folderName + libary.jarName;

        if (new File(fullPath).exists()) {
            if (MD5Checksum.getMD5Checksum(fullPath).equals(libary.md5sum)) {
                return new File(fullPath);
            } else {
                new File(folderName).mkdirs();
                System.out.println("MD5 is Different Re Downloading Jar: " + fullPath);

                URL website = new URL(libary.url);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(fullPath);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                return new File(fullPath);
            }
        } else {
            new File(folderName).mkdirs();

            System.out.println("Downloading Jar: " + fullPath);

            URL website = new URL(libary.url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(fullPath);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return new File(fullPath);
        }
    }
}
