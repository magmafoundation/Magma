package org.magmafoundation.magma.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.magmafoundation.magma.commands.MagmaCommand;
import org.magmafoundation.magma.commands.TPSCommand;
import org.magmafoundation.magma.commands.VersionCommand;
import org.magmafoundation.magma.configuration.value.Value;
import org.magmafoundation.magma.configuration.value.values.BooleanValue;
import org.magmafoundation.magma.configuration.value.values.IntValue;
import org.magmafoundation.magma.configuration.value.values.StringArrayValue;
import org.magmafoundation.magma.configuration.value.values.StringValue;

/**
 * MagmaConfig
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:14 am
 */
public class MagmaConfig extends ConfigBase {

    public static MagmaConfig instance = new MagmaConfig();

    //============================Debug======================================
    public final BooleanValue debugPrintBukkitMatterials = new BooleanValue(this, "debug.debugPrintBukkitMatterials", false, "Prints the Forge Bukkit Materials");
    public final BooleanValue debugPrintBukkitBannerPatterns = new BooleanValue(this, "debug.debugPrintBukkitBannerPatterns", false, "Prints the Forge Bukkit Banner Patterns");
    public final BooleanValue debugPrintCommandNode = new BooleanValue(this, "debug.debugPrintCommandNode", false, "Prints out all Command Nodes for permissions");

    //============================Black List Mods=============================
    public final BooleanValue blacklistedModsEnable = new BooleanValue(this, "forge.blacklistedmods.enabled", false, "Enable blacklisting of mods");
    public final StringArrayValue blacklistedMods = new StringArrayValue(this, "forge.blacklistedmods.list", "", "A list of mods to blacklist");
    public final StringValue blacklistedModsKickMessage = new StringValue(this, "forge.blacklistedmods.kickmessage", "Please Remove Blacklisted Mods", "Mod Blacklist kick message");

    //=============================WORLD SETTINGS=============================
    public final IntValue expMergeMaxValue = new IntValue(this, "experience-merge-max-value", -1,
        "Instructs the server put a maximum value on experience orbs, preventing them all from merging down into 1 single orb.");

    //=============================Misc SETTINGS=============================
    public final BooleanValue forgeBukkitPermissionHandlerEnable = new BooleanValue(this, "forge.bukkitPermissionHandler.enable", true, "Let's Bukkit permission plugins handle forge/modded commands");
    public final BooleanValue magmaAutoUpdater = new BooleanValue(this, "magma.auto-update", true, "Auto updates the Magma jar");
    public final StringValue magmaAutoUpdaterPath = new StringValue(this, "magma.auto-update-path", "", "Path to place new jar");

    public final BooleanValue overrideServerBrand = new BooleanValue(this, "magma.advanced.override-brand", false, "Enables or disables overriding the server brand");
    public final StringValue serverBrandValue = new StringValue(this, "magma.advanced.override-value", "Spigot", "Value to use for server brand when overriding is enabled");

    private final String HEADER = "This is the main configuration file for Magma.\n" +
        "\n" +
        "Site: https://magmafoundation.org\n" +
        "Discord: https://discord.gg/6rkqngA\n";


    public MagmaConfig() {
        super("magma.yml", "magma");
        init();
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
        load();
    }

    @Override
    protected void addCommands() {
        commands.put("magma", new MagmaCommand("magma"));
        commands.put("tps", new TPSCommand("tps"));
        commands.put("version", new VersionCommand("version"));
    }

    @Override
    protected void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            StringBuilder header = new StringBuilder(HEADER + "\n");
            for (Value toggle : values.values()) {
                if (!toggle.description.equals("")) {
                    header.append("Value: ").append(toggle.path).append(" Default: ").append(toggle.key).append("   # ").append(toggle.description).append("\n");
                }

                config.addDefault(toggle.path, toggle.key);
                values.get(toggle.path).setValues(config.getString(toggle.path));
            }
            version = getInt("config-version", 2);
            set("config-version", 2);

            config.options().header(header.toString());
            config.options().copyDefaults(true);

            this.save();
        } catch (Exception ex) {
            MinecraftServer.getServerInstance().server.getLogger()
                .log(Level.SEVERE, "Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}
