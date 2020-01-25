package org.magmafoundation.magma.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * MagmaUnsafeValues
 *
 * @author Redned
 * @since 25/01/2019 - 03:21 pm
 */
public class MagmaUnsafeValues implements UnsafeValues {

    private static final List<String> SUPPORTED_API_VERSIONS = Arrays.asList("1.13", "1.14");

    private static final MagmaUnsafeValues INSTANCE = new MagmaUnsafeValues();

    @Override
    public Material toLegacy(Material material) {
        return material; // TODO
    }

    @Override
    public Material fromLegacy(Material material) {
        return material; // TODO
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        return material.getItemType(); // TODO
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        return material.getItemType(); // TODO
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        return null; // TODO
    }

    @Override
    public Material getMaterial(String material, int version) {
        return Material.matchMaterial(material); // TODO
    }

    @Override
    public int getDataVersion() {
        return 0; // TODO
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        return stack; // TODO
    }

    @Override
    public void checkSupported(PluginDescriptionFile file) throws InvalidPluginException {
        if (file.getAPIVersion() != null) {
            if (!SUPPORTED_API_VERSIONS.contains(file.getAPIVersion())) {
                throw new InvalidPluginException("Unsupported API version " + file.getAPIVersion() + "! Supported versions: ");
            }
        } else {
            // initialize unsafe values // TODO
            Bukkit.getLogger().log(Level.WARNING, "Legacy plugin " + file.getFullName() + " does not specify an api-version.");
        }
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        return new byte[0];
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        return null; // TODO
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        return false; // TODO
    }

    @Override
    public String getTimingsServerName() {
        return null; // TODO
    }

    public static MagmaUnsafeValues getInstance() {
        return INSTANCE;
    }
}
