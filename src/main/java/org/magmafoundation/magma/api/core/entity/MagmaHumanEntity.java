package org.magmafoundation.magma.api.core.entity;

import com.google.common.base.Preconditions;
import java.util.Collection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.HandSide;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.*;
import org.bukkit.inventory.InventoryView.Property;
import org.magmafoundation.magma.api.bridge.inventory.container.IBridgeContainer;
import org.magmafoundation.magma.api.bridge.util.IBridgeCooldownTracker;
import org.magmafoundation.magma.api.core.MagmaServer;
import org.magmafoundation.magma.api.core.inventory.MagmaInventory;
import org.magmafoundation.magma.api.core.inventory.MagmaItemStack;
import org.magmafoundation.magma.api.core.inventory.MagmaPlayerInventory;
import org.magmafoundation.magma.api.core.util.Cooldown;
import org.magmafoundation.magma.api.core.util.MagmaMagicNumbers;

/**
 * MagmaHumanEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 08:11 pm
 */
public class MagmaHumanEntity extends MagmaLivingEntity implements HumanEntity {

    private MagmaPlayerInventory inventory;
    private final MagmaInventory enderChest;
    private GameMode gameMode;

    // TODO: 27/11/2019 Add Permissions & Op


    public MagmaHumanEntity(MagmaServer server,
        PlayerEntity entity) {
        super(server, entity);
        gameMode = server.getDefaultGameMode();
        this.inventory = new MagmaPlayerInventory(entity.inventory);
        enderChest = new MagmaInventory(entity.getInventoryEnderChest());
    }

    @Override
    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public Inventory getEnderChest() {
        return enderChest;
    }

    @Override
    public MainHand getMainHand() {
        return getHandle().getPrimaryHand() == HandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        return false;
    }

    @Override
    public InventoryView getOpenInventory() {
        return ((IBridgeContainer) getHandle().container).getBukkitView();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        throw new UnsupportedOperationException("Not Implemented yet");

    }

    @Override
    public void openInventory(InventoryView inventory) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public InventoryView openMerchant(Villager trader, boolean force) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void closeInventory() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        getInventory().setItemInHand(item);
    }

    @Override
    public ItemStack getItemOnCursor() {
        return MagmaItemStack.asCraftMirror(getHandle().inventory.getItemStack());
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        net.minecraft.item.ItemStack stack = MagmaItemStack.asNMSCopy(item);
        getHandle().inventory.setPickedItemStack(stack);
        if (this instanceof MagmaPlayer) {
            ((ServerPlayerEntity) getHandle()).updateHeldItem();
        }
    }

    @Override
    public boolean hasCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");
        return getHandle().getCooldownTracker().hasCooldown(MagmaMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");
        Cooldown cooldown = ((IBridgeCooldownTracker) getHandle().getCooldownTracker())
            .getCooldowns().get(MagmaMagicNumbers.getItem(material));

        return (cooldown == null) ? 0 : Math.max(0,
            cooldown.expireTicks - ((IBridgeCooldownTracker) getHandle().getCooldownTracker())
                .getCurrentTick());
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        Preconditions.checkArgument(material != null, "material");
        Preconditions.checkArgument(ticks >= 0, "Cannot have negative cooldown");

        getHandle().getCooldownTracker().setCooldown(MagmaMagicNumbers.getItem(material), ticks);
    }

    @Override
    public int getSleepTicks() {
        return getHandle().getSleepTimer();
    }

    @Override
    public Location getBedSpawnLocation() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public boolean sleep(Location location, boolean force) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public Location getBedLocation() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public GameMode getGameMode() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void setGameMode(GameMode mode) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public boolean isBlocking() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public boolean isHandRaised() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public int getExpToLevel() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public Entity getShoulderEntityLeft() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void setShoulderEntityLeft(Entity entity) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public Entity getShoulderEntityRight() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public void setShoulderEntityRight(Entity entity) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public PlayerEntity getHandle() {
        return (PlayerEntity) entity;
    }
}
