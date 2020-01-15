package org.magmafoundation.magma.api.core.inventory;

import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.magmafoundation.magma.api.bridge.item.IBridgeItemStack;
import org.magmafoundation.magma.api.core.util.MagmaMagicNumbers;

/**
 * MagmatemStack
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 26/11/2019 - 07:29 am
 */
@DelegateDeserialization(ItemStack.class)
public final class MagmaItemStack extends ItemStack {

    public static net.minecraft.item.ItemStack asNMSCopy(ItemStack original) {
        if (original instanceof MagmaItemStack) {
            MagmaItemStack stack = (MagmaItemStack) original;
            return stack.handle == null ? net.minecraft.item.ItemStack.EMPTY : stack.handle.copy();
        }
        if (original == null || original.getType() == Material.AIR) {
            return net.minecraft.item.ItemStack.EMPTY;
        }

        Item item = MagmaMagicNumbers.getItem(original.getType(), original.getDurability());

        if (item == null) {
            return net.minecraft.item.ItemStack.EMPTY;
        }

        net.minecraft.item.ItemStack stack = new net.minecraft.item.ItemStack(item,
            original.getAmount());
        if (original.hasItemMeta()) {
            setItemMeta(stack, original.getItemMeta());
        }
        return stack;
    }

    public static net.minecraft.item.ItemStack copyNMSStack(net.minecraft.item.ItemStack original,
        int amount) {
        net.minecraft.item.ItemStack stack = original.copy();
        stack.setCount(amount);
        return stack;
    }

    /**
     * Copies the NMS stack to return as a strictly-Bukkit stack
     */
    public static ItemStack asBukkitCopy(net.minecraft.item.ItemStack original) {
        if (original.isEmpty()) {
            return new ItemStack(Material.AIR);
        }
        ItemStack stack = new ItemStack(MagmaMagicNumbers.getMaterial(original.getItem()),
            original.getCount());
        if (hasItemMeta(original)) {
            stack.setItemMeta(getItemMeta(original));
        }
        return stack;
    }

    public static MagmaItemStack asCraftMirror(net.minecraft.item.ItemStack original) {
        return new MagmaItemStack((original == null || original.isEmpty()) ? null : original);
    }

    public static MagmaItemStack asCraftCopy(ItemStack original) {
        if (original instanceof MagmaItemStack) {
            MagmaItemStack stack = (MagmaItemStack) original;
            return new MagmaItemStack(stack.handle == null ? null : stack.handle.copy());
        }
        return new MagmaItemStack(original);
    }

    public static MagmaItemStack asNewCraftStack(Item item) {
        return asNewCraftStack(item, 1);
    }

    public static MagmaItemStack asNewCraftStack(Item item, int amount) {
        return new MagmaItemStack(MagmaMagicNumbers.getMaterial(item), amount, (short) 0, null);
    }

    net.minecraft.item.ItemStack handle;

    /**
     * Mirror
     */
    private MagmaItemStack(net.minecraft.item.ItemStack item) {
        this.handle = item;
    }

    private MagmaItemStack(ItemStack item) {
        this(item.getType(), item.getAmount(), item.getDurability(),
            item.hasItemMeta() ? item.getItemMeta() : null);
    }

    private MagmaItemStack(Material type, int amount, short durability, ItemMeta itemMeta) {
        setType(type);
        setAmount(amount);
        setDurability(durability);
        setItemMeta(itemMeta);
    }

    @Override
    public MaterialData getData() {
        return handle != null ? MagmaMagicNumbers.getMaterialData(handle.getItem())
            : super.getData();
    }

    @Override
    public Material getType() {
        return handle != null ? MagmaMagicNumbers.getMaterial(handle.getItem()) : Material.AIR;
    }

    @Override
    public void setType(Material type) {
        if (getType() == type) {
            return;
        } else if (type == Material.AIR) {
            handle = null;
        } else if (MagmaMagicNumbers.getItem(type) == null) { // :(
            handle = null;
        } else if (handle == null) {
            handle = new net.minecraft.item.ItemStack(MagmaMagicNumbers.getItem(type), 1);
        } else {
            ((IBridgeItemStack) handle.getItem()).setItem(MagmaMagicNumbers.getItem(type));
            if (hasItemMeta()) {
                // This will create the appropriate item meta, which will contain all the data we intend to keep
                setItemMeta(handle, getItemMeta(handle));
            }
        }
        setData(null);
    }

    @Override
    public int getAmount() {
        return handle != null ? handle.getCount() : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (handle == null) {
            return;
        }

        handle.setCount(amount);
        if (amount == 0) {
            handle = null;
        }
    }

    @Override
    public void setDurability(final short durability) {
        // Ignore damage if item is null
        if (handle != null) {
            handle.setDamage(durability);
        }
    }

    @Override
    public short getDurability() {
        if (handle != null) {
            return (short) handle.getDamage();
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return (handle == null) ? Material.AIR.getMaxStackSize()
            : handle.getItem().getMaxStackSize();
    }

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        Validate.notNull(ench, "Cannot add null enchantment");
        // TODO: 26/11/2019 Implement Enchantment
    }

    static boolean makeTag(net.minecraft.item.ItemStack item) {
        if (item == null) {
            return false;
        }

        if (item.getTag() == null) {
            item.setTag(new CompoundNBT());
        }

        return true;
    }

    @Override
    public boolean containsEnchantment(Enchantment ench) {
        return getEnchantmentLevel(ench) > 0;
    }

    @Override
    public int getEnchantmentLevel(Enchantment ench) {
        Validate.notNull(ench, "Cannot find null enchantment");
        // TODO: 26/11/2019 Implement Enchantment Handler
        return -1;
    }

    @Override
    public int removeEnchantment(Enchantment ench) {
        Validate.notNull(ench, "Cannot remove null enchantment");
        // TODO: 26/11/2019 Implement Enchantment
        return -1;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return getEnchantments(handle);
    }

    static Map<Enchantment, Integer> getEnchantments(net.minecraft.item.ItemStack item) {
        // TODO: 26/11/2019 Implement Enchantment
        return null;
    }

    static ListNBT getEnchantmentList(net.minecraft.item.ItemStack item) {
        // TODO: 26/11/2019 Implement Enchantment
        return null;
    }

    @Override
    public MagmaItemStack clone() {
        MagmaItemStack itemStack = (MagmaItemStack) super.clone();
        if (this.handle != null) {
            itemStack.handle = this.handle.copy();
        }
        return itemStack;
    }

    @Override
    public ItemMeta getItemMeta() {
        return getItemMeta(handle);
    }

    public static ItemMeta getItemMeta(net.minecraft.item.ItemStack item) {
        if (!hasItemMeta(item)) {
//            return MagmaItemFactory.instance().getItemMeta(getType(item));
        }
        switch (getType(item)) {
            case WRITTEN_BOOK:
                return null; // TODO: 26/11/2019 Add MagmaMetaBookSigned
            case WRITABLE_BOOK:
                return null; // TODO: 26/11/2019 Add MagmaMetaBook
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return null; // TODO: 26/11/2019 Add MagmaMetaSkull
            case LEATHER_HELMET:
            case LEATHER_HORSE_ARMOR:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return null; // TODO: 26/11/2019 Add MagmaMetaLeatherArmor
            case POTION:
            case SPLASH_POTION:
            case LINGERING_POTION:
            case TIPPED_ARROW:
                return null; // TODO: 26/11/2019 Add MagmaMetaPotion
            case FILLED_MAP:
                return null; // TODO: 26/11/2019 Add MagmaMetaMap
            case FIREWORK_ROCKET:
                return null; // TODO: 26/11/2019 Add MagmaMetaFirework
            case FIREWORK_STAR:
                return null; // TODO: 26/11/2019 Add MagmaMetaCharge
            case ENCHANTED_BOOK:
                return null; // TODO: 26/11/2019 Add MagmaMetaEnchantedBook
            case BLACK_BANNER:
            case BLACK_WALL_BANNER:
            case BLUE_BANNER:
            case BLUE_WALL_BANNER:
            case BROWN_BANNER:
            case BROWN_WALL_BANNER:
            case CYAN_BANNER:
            case CYAN_WALL_BANNER:
            case GRAY_BANNER:
            case GRAY_WALL_BANNER:
            case GREEN_BANNER:
            case GREEN_WALL_BANNER:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_BANNER:
            case MAGENTA_WALL_BANNER:
            case ORANGE_BANNER:
            case ORANGE_WALL_BANNER:
            case PINK_BANNER:
            case PINK_WALL_BANNER:
            case PURPLE_BANNER:
            case PURPLE_WALL_BANNER:
            case RED_BANNER:
            case RED_WALL_BANNER:
            case WHITE_BANNER:
            case WHITE_WALL_BANNER:
            case YELLOW_BANNER:
            case YELLOW_WALL_BANNER:
                return null; // TODO: 26/11/2019 Add MagmaMetaBanner
            case BAT_SPAWN_EGG:
            case BLAZE_SPAWN_EGG:
            case CAT_SPAWN_EGG:
            case CAVE_SPIDER_SPAWN_EGG:
            case CHICKEN_SPAWN_EGG:
            case COD_SPAWN_EGG:
            case COW_SPAWN_EGG:
            case CREEPER_SPAWN_EGG:
            case DOLPHIN_SPAWN_EGG:
            case DONKEY_SPAWN_EGG:
            case DROWNED_SPAWN_EGG:
            case ELDER_GUARDIAN_SPAWN_EGG:
            case ENDERMAN_SPAWN_EGG:
            case ENDERMITE_SPAWN_EGG:
            case EVOKER_SPAWN_EGG:
            case FOX_SPAWN_EGG:
            case GHAST_SPAWN_EGG:
            case GUARDIAN_SPAWN_EGG:
            case HORSE_SPAWN_EGG:
            case HUSK_SPAWN_EGG:
            case LLAMA_SPAWN_EGG:
            case MAGMA_CUBE_SPAWN_EGG:
            case MOOSHROOM_SPAWN_EGG:
            case MULE_SPAWN_EGG:
            case OCELOT_SPAWN_EGG:
            case PANDA_SPAWN_EGG:
            case PARROT_SPAWN_EGG:
            case PHANTOM_SPAWN_EGG:
            case PIG_SPAWN_EGG:
            case PILLAGER_SPAWN_EGG:
            case POLAR_BEAR_SPAWN_EGG:
            case PUFFERFISH_SPAWN_EGG:
            case RABBIT_SPAWN_EGG:
            case RAVAGER_SPAWN_EGG:
            case SALMON_SPAWN_EGG:
            case SHEEP_SPAWN_EGG:
            case SHULKER_SPAWN_EGG:
            case SILVERFISH_SPAWN_EGG:
            case SKELETON_HORSE_SPAWN_EGG:
            case SKELETON_SPAWN_EGG:
            case SLIME_SPAWN_EGG:
            case SPIDER_SPAWN_EGG:
            case SQUID_SPAWN_EGG:
            case STRAY_SPAWN_EGG:
            case TRADER_LLAMA_SPAWN_EGG:
            case TROPICAL_FISH_SPAWN_EGG:
            case TURTLE_SPAWN_EGG:
            case VEX_SPAWN_EGG:
            case VILLAGER_SPAWN_EGG:
            case VINDICATOR_SPAWN_EGG:
            case WANDERING_TRADER_SPAWN_EGG:
            case WITCH_SPAWN_EGG:
            case WITHER_SKELETON_SPAWN_EGG:
            case WOLF_SPAWN_EGG:
            case ZOMBIE_HORSE_SPAWN_EGG:
            case ZOMBIE_PIGMAN_SPAWN_EGG:
            case ZOMBIE_SPAWN_EGG:
            case ZOMBIE_VILLAGER_SPAWN_EGG:
                return null; // TODO: 26/11/2019 Add MagmaMetaSpawnEgg
            case ARMOR_STAND:
                return null; // TODO: 26/11/2019 Add MagmaMetaArmorStand
            case KNOWLEDGE_BOOK:
                return null; // TODO: 26/11/2019 Add MagmaMetaKnowledgeBook
            case FURNACE:
            case CHEST:
            case TRAPPED_CHEST:
            case JUKEBOX:
            case DISPENSER:
            case DROPPER:
            case ACACIA_SIGN:
            case ACACIA_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_WALL_SIGN:
            case SPAWNER:
            case BREWING_STAND:
            case ENCHANTING_TABLE:
            case COMMAND_BLOCK:
            case REPEATING_COMMAND_BLOCK:
            case CHAIN_COMMAND_BLOCK:
            case BEACON:
            case DAYLIGHT_DETECTOR:
            case HOPPER:
            case COMPARATOR:
            case SHIELD:
            case STRUCTURE_BLOCK:
            case SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case ENDER_CHEST:
            case BARREL:
            case BELL:
            case BLAST_FURNACE:
            case CAMPFIRE:
            case JIGSAW:
            case LECTERN:
            case SMOKER:
                return null; // TODO: 26/11/2019 Add MagmaMetaBlockState
            case TROPICAL_FISH_BUCKET:
                return null; // TODO: 26/11/2019 Add MagmaMetaTropicalFishBucket
            case CROSSBOW:
                return null; // TODO: 26/11/2019 Add MagmaMetaCrossbow
            case SUSPICIOUS_STEW:
                return null; // TODO: 26/11/2019 Add MagmaMetaSuspiciousStew
            default:
                return null; // TODO: 26/11/2019 Add MagmaMetaItem
        }
    }

    static Material getType(net.minecraft.item.ItemStack item) {
        return null; // TODO: 26/11/2019 Implement
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        return setItemMeta(handle, itemMeta);
    }

    public static boolean setItemMeta(net.minecraft.item.ItemStack item, ItemMeta itemMeta) {
        return false; // TODO: 26/11/2019 Implement
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        return false; // TODO: 26/11/2019 Implement
    }

    @Override
    public boolean hasItemMeta() {
        return false; // TODO: 26/11/2019 Implement
    }

    static boolean hasItemMeta(net.minecraft.item.ItemStack item) {
        return !(item == null || item.getTag() == null || item.getTag().isEmpty());
    }
}

