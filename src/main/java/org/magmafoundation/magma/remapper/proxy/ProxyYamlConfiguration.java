package org.magmafoundation.magma.remapper.proxy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.reader.ReaderException;

/**
 * ProxyYamlConfiguration
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 21/11/2019 - 08:03 pm
 */
public class ProxyYamlConfiguration {

    private static final Charset GBK = Charset.forName("GBK");
    private static final Charset defaultCharset = Charset.defaultCharset();
    private static final Charset otherCharse = defaultCharset.name().equalsIgnoreCase("GBK") ? StandardCharsets.UTF_8 : GBK;

    public static YamlConfiguration loadConfiguration(InputStream inputStream) {
        ReaderException readerException = null;
        try {
            return YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, defaultCharset));
        } catch (ReaderException e) {
            readerException = e;
            try {
                return YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, otherCharse));
            } catch (ReaderException e2) {
                throw readerException;
            }
        }
    }
}
