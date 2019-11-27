package org.magmafoundation.magma.api.core.inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.magmafoundation.magma.api.accessor.inventory.IMagmaInventory;
import org.magmafoundation.magma.api.core.util.MagmaLegacy;

/**
 * MagmaInventory
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 26/11/2019 - 07:14 am
 */
public class MagmaInventory implements Inventory {

    protected final IMagmaInventory inventory;

    public MagmaInventory(IMagmaInventory inventory) {
        this.inventory = inventory;
    }

    public IMagmaInventory getInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getInventory().getSizeInventory();
    }

    @Override
    public int getMaxStackSize() {
        return getInventory().getInventoryStackLimit();
    }

    @Override
    public void setMaxStackSize(int size) {
        getInventory().setMaxStackSize(size);
    }

    @Override
    public ItemStack getItem(int index) {
        net.minecraft.item.ItemStack itemStack = getInventory().getStackInSlot(index);
        return itemStack.isEmpty() ? null : MagmaItemStack.asCraftMirror(itemStack);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        getInventory().setInventorySlotContents(index, MagmaItemStack.asNMSCopy(item));
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
        Validate.notNull(items, "Item cannot be null");
        HashMap<Integer, ItemStack> leftOver = new HashMap<>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];

            while (true) {
                //check if we have a stack of it
                int firstPartial = firstPartial(item);
                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = firstEmpty();

                    if (firstFree == -1) {
                        // No space at all!
                        leftOver.put(i, item);
                        break;
                    } else {
                        // More than a single stack!
                        if (item.getAmount() > getMaxItemStack()) {
                            MagmaItemStack stack = MagmaItemStack.asCraftCopy(item);
                            stack.setAmount(getMaxItemStack());
                            setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - getMaxItemStack());
                        } else {
                            // Just store it
                            setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();

                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        // To make sure the packet is sent to the client
                        setItem(firstPartial, partialItem);
                        break;
                    }

                    // It fits partially
                    partialItem.setAmount(maxAmount);
                    // To make sure the packet is sent to the client
                    setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftOver;
    }

    public int firstPartial(Material material) {
        Validate.notNull(material, "Material cannot be null");
        material = MagmaLegacy.fromLegacy(material);
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material && item.getAmount() < item
                .getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = getStorageContents();
        ItemStack filteredItem = MagmaItemStack.asCraftCopy(item);
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < inventory.length; i++) {
            ItemStack cItem = inventory[i];
            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem
                .isSimilar(filteredItem)) {
                return i;
            }
        }
        return -1;
    }

    private int getMaxItemStack() {
        return getInventory().getInventoryStackLimit();
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items)
        throws IllegalArgumentException {
        Validate.notNull(items, "Items cannot be null");
        HashMap<Integer, ItemStack> leftOver = new HashMap<>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = first(item, false);

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftOver.put(i, item);
                    break;
                } else {
                    ItemStack itemStack = getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        clear(first);
                    } else {
                        // split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        setItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                // Bail when done
                if (toDelete <= 0) {
                    break;
                }
            }
        }
        return leftOver;
    }

    protected ItemStack[] asCraftMirror(List<net.minecraft.item.ItemStack> mcItems) {
        int size = mcItems.size();
        ItemStack[] items = new ItemStack[size];

        IntStream.range(0, size).forEach(i -> {
            net.minecraft.item.ItemStack mcItem = mcItems.get(i);
            items[i] = (mcItem.isEmpty()) ? null : MagmaItemStack.asCraftMirror(mcItem);
        });

        return items;
    }

    @Override
    public ItemStack[] getContents() {
        return asCraftMirror(getInventory().getContents());
    }

    @Override
    public void setContents(ItemStack[] items) throws IllegalArgumentException {
        if (getSize() < items.length) {
            throw new IllegalArgumentException(
                "Invalid inventory size; expected " + getSize() + " or less");
        }

        IntStream.range(0, getSize()).forEach(i -> {
            if (i >= items.length) {
                setItem(i, null);
            } else {
                setItem(i, items[i]);
            }
        });
    }

    @Override
    public ItemStack[] getStorageContents() {
        return getContents();
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        setContents(items);
    }

    @Override
    public boolean contains(Material material) throws IllegalArgumentException {
        Validate.notNull(material, "Material cannot be null");
        material = MagmaLegacy.fromLegacy(material);
        for (ItemStack item : getStorageContents()) {
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        }
        return Arrays.asList(getStorageContents()).contains(item);
    }

    @Override
    public boolean contains(Material material, int amount) throws IllegalArgumentException {
        Validate.notNull(material, "Material cannot be null");
        material = MagmaLegacy.fromLegacy(material);
        if (amount <= 0) {
            return true;
        }
        for (ItemStack item : getStorageContents()) {
            if (item != null && item.getType() == material && (amount -= item.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.equals(i) && --amount <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material)
        throws IllegalArgumentException {
        Validate.notNull(material, "Material cannot be null");
        material = MagmaLegacy.fromLegacy(material);
        HashMap<Integer, ItemStack> slots = new HashMap<>();

        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material) {
                slots.put(i, item);
            }
        }
        return slots;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<>();
        if (item != null) {
            ItemStack[] inventory = getStorageContents();
            slots = IntStream.range(0, inventory.length).filter(i -> item.equals(inventory[i]))
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> inventory[i], (a, b) -> b, HashMap::new));
        }
        return slots;
    }

    @Override
    public int first(Material material) throws IllegalArgumentException {
        Validate.notNull(material, "Material cannot be null");
        material = MagmaLegacy.fromLegacy(material);
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(ItemStack item) {
        return first(item, true);
    }

    @Override
    public int firstEmpty() {
        ItemStack[] inventory = getStorageContents();
        return IntStream.range(0, inventory.length).filter(i -> inventory[i] == null).findFirst()
            .orElse(-1);
    }


    private int first(ItemStack item, boolean withAmount) {
        if (item == null) {
            return -1;
        }
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                continue;
            }

            if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public void remove(Material material) throws IllegalArgumentException {
        Validate.notNull(material, "Material cannot be null");
        material = MagmaLegacy.fromLegacy(material);
        ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() == material) {
                clear(i);
            }
        }
    }

    @Override
    public void remove(ItemStack item) {
        ItemStack[] items = getStorageContents();
        IntStream.range(0, items.length).filter(i -> items[i] != null && items[i].equals(item))
            .forEach(this::clear);
    }

    @Override
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        IntStream.range(0, getSize()).forEach(this::clear);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.inventory.getViewers();
    }

    @Override
    public InventoryType getType() {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    @Override
    public InventoryHolder getHolder() {
        return inventory.getOwner();
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            index += getSize() + 1; // ie, with -1, previous() will return the last element
        }
        return new InventoryIterator(this, index);
    }

    @Override
    public Location getLocation() {
        return inventory.getLocation();
    }

    @Override
    public int hashCode() {
        return inventory.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MagmaInventory && ((MagmaInventory) obj).inventory
            .equals(this.inventory);
    }
}
