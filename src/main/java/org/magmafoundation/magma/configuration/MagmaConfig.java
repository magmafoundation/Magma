package org.magmafoundation.magma.configuration;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.magmafoundation.magma.Metrics;
import org.magmafoundation.magma.Metrics.SimplePie;
import org.magmafoundation.magma.api.ServerAPI;
import org.magmafoundation.magma.commands.MagmaCommand;
import org.magmafoundation.magma.commands.VersionCommand;
import org.magmafoundation.magma.configuration.value.Value;
import org.magmafoundation.magma.configuration.value.values.BooleanValue;
import org.magmafoundation.magma.configuration.value.values.IntValue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

/**
 * MagmaConfig
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:14 am
 */
public class MagmaConfig extends ConfigBase {

    public static MagmaConfig instance;
    private static Metrics metrics;
    public final BooleanValue debugPrintBukkitMatterials = new BooleanValue(this, "debug.debugPrintBukkitMatterials", false, "Prints the Forge Bukkit Materials");

    //=============================WORLD SETTINGS=============================
    public final IntValue expMergeMaxValue = new IntValue(this, "experience-merge-max-value", -1, "Instructs the server put a maximum value on experience orbs, preventing them all from merging down into 1 single orb.");
    //=============================WORLD SETTINGS=============================
    private final String HEADER = "This is the main configuration file for Magma.\n" +
            "\n" +
            "Site: https://magmafoundation.org\n" +
            "Discord: https://discord.gg/6rkqngA\n";

    public MagmaConfig() {
        super("magma.yml", "magma");
        init();
        instance = this;
    }

    public static String getString(String s, String key, String defaultreturn) {
        if (s.contains(key)) {
            String string = s.substring(s.indexOf(key));
            String s1 = (string.substring(string.indexOf(": ") + 2));
            String[] ss = s1.split("\n");
            return ss[0].trim().replace("'", "").replace("\"", "");
        }
        return defaultreturn;
    }

    public static String getString(File f, String key, String defaultreturn) {
        try {
            String s = FileUtils.readFileToString(f, "UTF-8");
            if (s.contains(key)) {
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(": ") + 2));
                String[] ss = s1.split("\n");
                return ss[0].trim().replace("'", "").replace("\"", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public void init() {
        for (Field f : this.getClass().getFields()) {
            if (Modifier.isFinal(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())) {
                try {
                    Value value = (Value) f.get(this);
                    if (value == null) {
                        continue;
                    }
                    values.put(value.path, value);
                } catch (ClassCastException e) {
                } catch (Throwable t) {
                    System.out.println("[Magma] Failed to initialize a MagmaConfig values.");
                    t.printStackTrace();
                }
            }
        }
        if (metrics == null) {
            metrics = new Metrics();
            metrics.addCustomChart(new SimplePie("number_of_mods", () -> String.valueOf(ServerAPI.getModSize()))); // Report how many mods are running
        }
        load();
    }

    @Override
    protected void addCommands() {
        commands.put("magma", new MagmaCommand("magma"));
        commands.put("version", new VersionCommand("version"));
    }

    @Override
    protected void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            String header = HEADER + "\n";
            for (Value toggle : values.values()) {
                if (!toggle.description.equals("")) {
                    header += "Value: " + toggle.path + " Default: " + toggle.key + "   # " + toggle.description + "\n";
                }

                config.addDefault(toggle.path, toggle.key);
                values.get(toggle.path).setValues(config.getString(toggle.path));
            }
            config.options().header(header);
            config.options().copyDefaults(true);

            version = getInt("config-version", 1);
            set("config-version", 1);

            this.save();
        } catch (Exception ex) {
            MinecraftServer.getServerInstance().server.getLogger()
                    .log(Level.SEVERE, "Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}
