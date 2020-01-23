package org.magmafoundation.magma.mixin.core.minecraft.server.dedicated;

import com.google.common.collect.Lists;

import com.google.common.collect.Sets;

import net.minecraft.server.ServerPropertiesProvider;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.magmafoundation.magma.Magma;
import org.magmafoundation.magma.mixin.core.minecraft.server.MixinMinecraftServer;
import org.magmafoundation.magma.plugin.MagmaPluginManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * MixinDedicatedServer
 *
 * @author Redned
 * @since 22/01/2020 - 06:21 am
 */
@Mixin(DedicatedServer.class)
@Implements(@Interface(iface = Server.class, prefix = "server$"))
public abstract class MixinDedicatedServer extends MixinMinecraftServer implements Server {

    @Shadow @Final private ServerPropertiesProvider settings;

    private MagmaPluginManager pluginManager = new MagmaPluginManager();

    @Override
    public String getName() {
        return Magma.getName();
    }

    @Override
    public String getVersion() {
        return Magma.getVersion();
    }

    @Override
    public String getBukkitVersion() {
        return Magma.getBukkitVersion();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return Lists.newArrayList((Collection<? extends Player>) (Object) playerList.getPlayers());
    }

    @Intrinsic
    public int server$getMaxPlayers() {
        return playerList.getMaxPlayers();
    }

    @Override
    public int getPort() {
        return serverPort;
    }

    @Override
    public int getViewDistance() {
        return settings.getProperties().viewDistance;
    }

    @Override
    public String getIp() {
        return settings.getProperties().serverIp;
    }

    @Override
    public String getWorldType() {
        return settings.getProperties().worldType.getName();
    }

    @Override
    public boolean getGenerateStructures() {
        return settings.getProperties().generateStructures;
    }

    @Override
    public boolean getAllowEnd() {
        return false;
    }

    @Override
    public boolean getAllowNether() {
        return settings.getProperties().allowNether;
    }

    @Override
    public boolean hasWhitelist() {
        return whitelistEnabled;
    }

    @Override
    public void setWhitelist(boolean value) {
        playerList.setWhiteListEnabled(value);
        this.whitelistEnabled = value;
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        Set<OfflinePlayer> offlinePlayers = new HashSet<>();
        for (String key : playerList.getWhitelistedPlayerNames()) {
            offlinePlayers.add(getOfflinePlayer(key));
        }
        return offlinePlayers;
    }

    @Override
    public void reloadWhitelist() {
        playerList.reloadWhitelist();
    }

    @Override
    public int broadcastMessage(String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    @Override
    public String getUpdateFolder() {
        return null;
    }

    @Override
    public File getUpdateFolderFile() {
        return null;
    }

    @Override
    public long getConnectionThrottle() {
        return 0;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public Player getPlayer(String name) {
        return (Player) playerList.getPlayerByUsername(name);
    }

    @Override
    public Player getPlayerExact(String name) {
        return null;
    }

    @Override
    public List<Player> matchPlayer(String name) {
        return null;
    }

    @Override
    public Player getPlayer(UUID id) {
        return (Player) playerList.getPlayerByUUID(id);
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public BukkitScheduler getScheduler() {
        return null;
    }

    @Override
    public ServicesManager getServicesManager() {
        return null;
    }

    @Override
    public List<World> getWorlds() {
        return (List<World>) (Object) worlds.values();
    }

    @Override
    public World createWorld(WorldCreator creator) {
        return null;
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        return false;
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        return false;
    }

    @Override
    public World getWorld(String name) {
        return null;
    }

    @Override
    public World getWorld(UUID uid) {
        return null;
    }

    @Override
    public MapView getMap(int id) {
        return null;
    }

    @Override
    public MapView createMap(World world) {
        return null;
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType) {
        return null;
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType, int radius, boolean findUnexplored) {
        return null;
    }

    @Intrinsic(displace = true)
    public void server$reload() {

    }

    @Override
    public void reloadData() {
        shadow$reload();
    }

    @Override
    public Logger getLogger() {
        return Magma.getLogger();
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        return null;
    }

    @Override
    public void savePlayers() {
        playerList.saveAllPlayerData();
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        return false;
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        return false;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        return null;
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return null;
    }

    @Override
    public void clearRecipes() {

    }

    @Override
    public void resetRecipes() {
        shadow$reload();
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        return null;
    }

    @Override
    public int getSpawnRadius() {
        return settings.getProperties().spawnProtection;
    }

    @Override
    public void setSpawnRadius(int value) {

    }

    @Override
    public boolean getOnlineMode() {
        return onlineMode;
    }

    @Override
    public boolean getAllowFlight() {
        return allowFlight;
    }

    @Override
    public boolean isHardcore() {
        return settings.getProperties().hardcore;
    }

    @Override
    public void shutdown() {
        initiateShutdown(false);
    }

    @Override
    public int broadcast(String message, String permission) {
        Set<CommandSender> recipients = new HashSet<>();
        for (Permissible permissible : getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                recipients.add((CommandSender) permissible);
            }
        }

        BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(!Bukkit.isPrimaryThread(), message, recipients);
        getPluginManager().callEvent(broadcastMessageEvent);
        if (broadcastMessageEvent.isCancelled()) {
            return 0;
        }

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(broadcastMessageEvent.getMessage());
        }

        return recipients.size();
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        return null;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        return null;
    }

    @Override
    public Set<String> getIPBans() {
        return Sets.newHashSet(playerList.getBannedIPs().getKeys());
    }

    @Override
    public void banIP(String address) {
        getBanList(BanList.Type.IP).addBan(address, null, null, null);
    }

    @Override
    public void unbanIP(String address) {
        getBanList(BanList.Type.IP).pardon(address);
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        Set<OfflinePlayer> bannedPlayers = new HashSet<>();
        for (String entry : playerList.getBannedPlayers().getKeys()) {
            bannedPlayers.add(getOfflinePlayer(entry));
        }
        return bannedPlayers;
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        switch (type) {
            case IP:
                return (BanList) playerList.getBannedIPs();
            case NAME:
            default:
                return (BanList) playerList.getBannedPlayers();
        }
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        Set<OfflinePlayer> offlinePlayers = new HashSet<>();
        for (String entry : playerList.getOppedPlayerNames()) {
            offlinePlayers.add(getOfflinePlayer(entry));
        }
        return offlinePlayers;
    }

    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(settings.getProperties().gamemode.getID());
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {
        for (World world : getWorlds()) {
            ((ServerWorld) world).getWorldInfo().setGameType(GameType.getByID(mode.getValue()));
        }
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return null;
    }

    @Override
    public File getWorldContainer() {
        return null;
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        return new OfflinePlayer[0];
    }

    @Override
    public Messenger getMessenger() {
        return null;
    }

    @Override
    public HelpMap getHelpMap() {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Merchant createMerchant(String title) {
        return null;
    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(serverThread);
    }

    @Override
    public String getMotd() {
        return motd == null ? "" : motd;
    }

    @Override
    public String getShutdownMessage() {
        return null;
    }

    @Override
    public Warning.WarningState getWarningState() {
        return null;
    }

    @Override
    public ItemFactory getItemFactory() {
        return null;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override
    public CachedServerIcon getServerIcon() {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
        return null;
    }

    @Override
    public void setIdleTimeout(int threshold) {
        setPlayerIdleTimeout(threshold);
    }

    @Override
    public int getIdleTimeout() {
        return getMaxPlayerIdleMinutes();
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        return null;
    }

    @Override
    public BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        return null;
    }

    @Override
    public KeyedBossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags) {
        return null;
    }

    @Override
    public Iterator<KeyedBossBar> getBossBars() {
        return null;
    }

    @Override
    public KeyedBossBar getBossBar(NamespacedKey key) {
        return null;
    }

    @Override
    public boolean removeBossBar(NamespacedKey key) {
        return false;
    }

    @Override
    public Entity getEntity(UUID uuid) {
        for (ServerWorld world : worlds.values()) {
            net.minecraft.entity.Entity entity = world.getEntityByUuid(uuid);
            if (entity != null) {
                return (Entity) entity;
            }
        }
        return null;
    }

    @Override
    public Advancement getAdvancement(NamespacedKey key) {
        return null;
    }

    @Override
    public Iterator<Advancement> advancementIterator() {
        return null;
    }

    @Override
    public BlockData createBlockData(Material material) {
        return null;
    }

    @Override
    public BlockData createBlockData(Material material, Consumer<BlockData> consumer) {
        return null;
    }

    @Override
    public BlockData createBlockData(String data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BlockData createBlockData(Material material, String data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public <T extends Keyed> Tag<T> getTag(String registry, NamespacedKey tag, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends Keyed> Iterable<Tag<T>> getTags(String registry, Class<T> clazz) {
        return null;
    }

    @Override
    public LootTable getLootTable(NamespacedKey key) {
        return null;
    }

    @Override
    public List<Entity> selectEntities(CommandSender sender, String selector) throws IllegalArgumentException {
        return null;
    }

    @Override
    public UnsafeValues getUnsafe() {
        return null;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return null;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        Bukkit.setServer(this);
    }
}
