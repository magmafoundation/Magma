package org.magmafoundation.magma.api.core.entity;

import java.util.Collection;
import net.minecraft.entity.player.PlayerEntity;
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
import org.magmafoundation.magma.api.core.MagmaServer;
import org.magmafoundation.magma.api.core.inventory.MagmaInventory;
import org.magmafoundation.magma.api.core.inventory.MagmaPlayerInventory;

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
        return null;
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        return null;
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        return null;
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        return null;
    }

    @Override
    public void openInventory(InventoryView inventory) {

    }

    @Override
    public InventoryView openMerchant(Villager trader, boolean force) {
        return null;
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        return null;
    }

    @Override
    public void closeInventory() {

    }

    @Override
    public ItemStack getItemInHand() {
        return null;
    }

    @Override
    public void setItemInHand(ItemStack item) {

    }

    @Override
    public ItemStack getItemOnCursor() {
        return null;
    }

    @Override
    public void setItemOnCursor(ItemStack item) {

    }

    @Override
    public boolean hasCooldown(Material material) {
        return false;
    }

    @Override
    public int getCooldown(Material material) {
        return 0;
    }

    @Override
    public void setCooldown(Material material, int ticks) {

    }

    @Override
    public int getSleepTicks() {
        return 0;
    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {

    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {

    }

    @Override
    public boolean sleep(Location location, boolean force) {
        return false;
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {

    }

    @Override
    public Location getBedLocation() {
        return null;
    }

    @Override
    public GameMode getGameMode() {
        return null;
    }

    @Override
    public void setGameMode(GameMode mode) {

    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isHandRaised() {
        return false;
    }

    @Override
    public int getExpToLevel() {
        return 0;
    }

    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        return false;
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        return 0;
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        return false;
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        return 0;
    }

    @Override
    public Entity getShoulderEntityLeft() {
        return null;
    }

    @Override
    public void setShoulderEntityLeft(Entity entity) {

    }

    @Override
    public Entity getShoulderEntityRight() {
        return null;
    }

    @Override
    public void setShoulderEntityRight(Entity entity) {

    }

    @Override
    public PlayerEntity getHandle() {
        return (PlayerEntity) entity;
    }
}
