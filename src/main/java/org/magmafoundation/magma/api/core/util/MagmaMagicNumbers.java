package org.magmafoundation.magma.api.core.util;

import static org.magmafoundation.magma.api.core.util.MagmaLegacy.toLegacyData;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.Dynamic;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.magmafoundation.magma.api.bridge.advancements.IAdvancementManager;
import org.magmafoundation.magma.api.bridge.server.IBridgeMinecraftServer;
import org.magmafoundation.magma.api.core.inventory.MagmaItemStack;

public class MagmaMagicNumbers implements UnsafeValues {

    public static final UnsafeValues INSTANCE = new MagmaMagicNumbers();

    private MagmaMagicNumbers() {
    }

    public static BlockState getBlock(MaterialData material) {
        return getBlock(material.getItemType(), material.getData());
    }

    public static BlockState getBlock(Material material, byte data) {
        return MagmaLegacy.fromLegacyData(MagmaLegacy.toLegacy(material), getBlock(material), data);
    }

    public static MaterialData getMaterial(BlockState data) {
        return MagmaLegacy.toLegacy(getMaterial(data.getBlock())).getNewData(toLegacyData(data));
    }

    public static Item getItem(Material material, short data) {
        if (material.isLegacy()) {
            return MagmaLegacy
                .fromLegacyData(MagmaLegacy.toLegacy(material), getItem(material), data);
        }

        return getItem(material);
    }

    public static MaterialData getMaterialData(Item item) {
        return MagmaLegacy.toLegacyData(getMaterial(item));
    }

    private static final Map<Block, Material> BLOCK_MATERIAL = new HashMap<>();
    private static final Map<Item, Material> ITEM_MATERIAL = new HashMap<>();
    private static final Map<Material, Item> MATERIAL_ITEM = new HashMap<>();
    private static final Map<Material, Block> MATERIAL_BLOCK = new HashMap<>();

    static {
        // TODO: 26/11/2019 Change this to forgeregistries  
        for (Block block : Registry.BLOCK) {
            BLOCK_MATERIAL.put(block,
                Material.getMaterial(Registry.BLOCK.getKey(block).getPath().toUpperCase(
                    Locale.ROOT)));
        }

        for (Item item : Registry.ITEM) {
            ITEM_MATERIAL.put(item, Material
                .getMaterial(Registry.ITEM.getKey(item).getPath().toUpperCase(Locale.ROOT)));
        }

        for (Material material : Material.values()) {
            ResourceLocation key = key(material);
            Registry.ITEM.getValue(key).ifPresent((item) -> {
                MATERIAL_ITEM.put(material, item);
            });
            Registry.BLOCK.getValue(key).ifPresent((block) -> {
                MATERIAL_BLOCK.put(material, block);
            });
        }
    }

    public static Material getMaterial(Block block) {
        return BLOCK_MATERIAL.get(block);
    }

    public static Material getMaterial(Item item) {
        return ITEM_MATERIAL.getOrDefault(item, Material.AIR);
    }

    public static Item getItem(Material material) {
        return MATERIAL_ITEM.get(material);
    }

    public static Block getBlock(Material material) {
        return MATERIAL_BLOCK.get(material);
    }

    public static ResourceLocation key(Material mat) {
        if (mat.isLegacy()) {
            mat = MagmaLegacy.fromLegacy(mat);
        }

        return MagmaNamespacedLocation.toMinecraft(mat.getKey());
    }

    @Override
    public Material toLegacy(Material material) {
        return MagmaLegacy.toLegacy(material);
    }

    @Override
    public Material fromLegacy(Material material) {
        return MagmaLegacy.fromLegacy(material);
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        return MagmaLegacy.fromLegacy(material);
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        return MagmaLegacy.fromLegacy(material, itemPriority);
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        throw new NotImplementedException("TODO: MagmaBlockData");
    }

    @Override
    public Material getMaterial(String material, int version) {
        Preconditions.checkArgument(version <= getDataVersion(),
            "Newer version! Server downgrades not supported!");
        // Fastpath up to date materials
        if (version == this.getDataVersion()) {
            return Material.getMaterial(material);
        }

        CompoundNBT stack = new CompoundNBT();
        stack.putString("id", "minecraft:" + material.toLowerCase(Locale.ROOT));

        Dynamic<INBT> converted = DataFixesManager.getDataFixer()
            .update(TypeReferences.ITEM_STACK, new Dynamic<>(NBTDynamicOps.INSTANCE, stack),
                version, this.getDataVersion());
        String newId = converted.get("id").asString("");

        return Material.matchMaterial(newId);
    }

    /**
     * This string should be changed if the NMS mappings do.
     *
     * It has no meaning and should only be used as an equality check. Plugins which are sensitive
     * to the NMS mappings may read it and refuse to load if it cannot be found or is different to
     * the expected value.
     *
     * Remember: NMS is not supported API and may break at any time for any reason irrespective of
     * this. There is often supported API to do the same thing as many common NMS usages. If not,
     * you are encouraged to open a feature and/or pull request for consideration, or use a well
     * abstracted third-party API such as ProtocolLib.
     *
     * @return string
     */
    public String getMappingsVersion() {
        return "11ae498d9cf909730659b6357e7c2afa";
    }

    @Override
    public int getDataVersion() {
        return SharedConstants.getVersion().getWorldVersion();
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.item.ItemStack nmsStack = MagmaItemStack.asNMSCopy(stack);

        try {
            nmsStack.setTag((CompoundNBT) JsonToNBT.getTagFromJson(arguments));
        } catch (CommandSyntaxException e) {
            Logger.getLogger(MagmaMagicNumbers.class.getName()).log(Level.SEVERE, null, e);
        }

        stack.setItemMeta(MagmaItemStack.getItemMeta(nmsStack));

        return stack;
    }

    private static final List<String> SUPPORTED_API = Arrays.asList("1.13", "1.14");

    @Override
    public void checkSupported(PluginDescriptionFile pdf, Server server)
        throws InvalidPluginException {
        String minimumVersion = ((IBridgeMinecraftServer) server).getMagmaServer().minimumAPI;
        int minimumIndex = SUPPORTED_API.indexOf(minimumVersion);

        if (pdf.getAPIVersion() != null) {
            int pluginIndex = SUPPORTED_API.indexOf(pdf.getAPIVersion());

            if (pluginIndex == -1) {
                throw new InvalidPluginException("Unsupported API version " + pdf.getAPIVersion());
            }

            if (pluginIndex < minimumIndex) {
                throw new InvalidPluginException("Plugin API version " + pdf.getAPIVersion()
                    + " is lower than the minimum allowed version. Please update or replace it.");
            }
        } else {
            if (minimumIndex == -1) {
                Bukkit.getLogger().log(Level.WARNING,
                    "Plugin " + pdf.getFullName() + " does not specify an api-version.");
            } else {
                throw new InvalidPluginException("Plugin API version " + pdf.getAPIVersion()
                    + " is lower than the minimum allowed version. Please update or replace it.");
            }
        }
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        return null;
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement,
        Server server) {
        if (Bukkit.getAdvancement(key) != null) {
            throw new IllegalArgumentException("Advancement " + key + " already exists.");
        }

        net.minecraft.advancements.Advancement.Builder nms = JSONUtils
            .fromJson(((IAdvancementManager) new AdvancementManager()).getGson(), advancement,
                net.minecraft.advancements.Advancement.Builder.class);
        if (nms != null) {
            ((IAdvancementManager) ((IBridgeMinecraftServer) server).getInstance()
                .getAdvancementManager()).getAdvancementList().loadAdvancements(Maps.newHashMap(
                Collections.singletonMap(MagmaNamespacedLocation.toMinecraft(key), nms)));
            Advancement bukkit = Bukkit.getAdvancement(key);

            if (bukkit != null) {
                File file = new File(
                    ((IBridgeMinecraftServer) server).getBukkitDataPackFolder(),
                    "data" + File.separator + key.getNamespace() + File.separator + "advancements"
                        + File.separator + key.getKey() + ".json");
                file.getParentFile().mkdirs();

                try {
                    Files.write(advancement, file, Charsets.UTF_8);
                } catch (IOException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "Error saving advancement " + key, ex);
                }

                ((IBridgeMinecraftServer) server).getInstance().getPlayerList().reloadResources();

                return bukkit;
            }
        }

        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key, Server server) {
        File file = new File(((IBridgeMinecraftServer) server).getBukkitDataPackFolder(),
            "data" + File.separator + key.getNamespace() + File.separator + "advancements"
                + File.separator + key.getKey() + ".json");
        return file.delete();
    }

}
