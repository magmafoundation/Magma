package org.magmafoundation.magma.configuration;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.magmafoundation.magma.Metrics;
import org.magmafoundation.magma.commands.MagmaCommand;
import org.magmafoundation.magma.commands.VersionCommand;
import org.magmafoundation.magma.configuration.value.Value;
import org.magmafoundation.magma.configuration.value.values.BooleanValue;

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

    private final String HEADER = "This is the main configuration file for Magma.\n" +
            "\n" +
            "Site: https://magmafoundation.org\n" +
            "Discord: https://discord.gg/6rkqngA\n";

    public static MagmaConfig instance;

    public final BooleanValue debugPrintBukkitMatterials = new BooleanValue(this, "debug.debugPrintBukkitMatterials", false, "Prints the Forge Bukkit Materials");

    private static Metrics metrics;

    public MagmaConfig() {
        super("magma.yml", "magma");
        init();
        instance = this;
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
        if(metrics == null){
            metrics = new Metrics();
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
            MinecraftServer.getServerInstance().server.getLogger().log(Level.SEVERE, "Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}
