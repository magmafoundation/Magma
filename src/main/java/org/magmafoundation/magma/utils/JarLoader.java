package org.magmafoundation.magma.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * LibraryDownloader
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 28/07/2020 - 07:32 pm
 */
public class JarLoader {

    private URLClassLoader urlClassLoader;

    public JarLoader(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    public void loadJar(URL url) throws Exception {
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(urlClassLoader, url);
    }

    public static void loadjar(JarLoader jarLoader, File file) throws Exception {
        jarLoader.loadJar(file.toURI().toURL());
    }
}
