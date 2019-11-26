package org.magmafoundation.magma.api.core;

import com.google.common.base.Preconditions;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SessionLockException;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.magmafoundation.magma.api.bridge.entity.IBridgeEntity;
import org.magmafoundation.magma.api.core.block.MagmaBlock;
import org.magmafoundation.magma.api.core.entity.MagmaPlayer;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * MagmaWorld
 *
 * @author Redned
 * @since 24/11/2019 - 01:05 pm
 */
public class MagmaWorld implements World {

    private final ServerWorld world;
    private WorldBorder worldBorder;
    private Environment environment;

    private final MagmaServer server = (MagmaServer) Bukkit.getServer();
    private final ChunkGenerator chunkGenerator;
    private final List<BlockPopulator> populators = new ArrayList<>();

    // TODO: private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);

    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int ambientSpawn = -1;

    private boolean pvpEnabled;
    private boolean keepSpawnInMemory;

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public MagmaWorld(ServerWorld world, ChunkGenerator chunkGenerator, Environment environment) {
        this.world = world;
        this.chunkGenerator = chunkGenerator;

        this.environment = environment;
        this.pvpEnabled = world.getServer().isPVPEnabled();
        this.keepSpawnInMemory = true;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return MagmaBlock.at(world, new BlockPos(x, y, z));
    }

    @Override
    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return 0;
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return 0;
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return null;
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return null;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return null;
    }

    @Override
    public Chunk getChunkAt(Block block) {
        return null;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        return false;
    }

    @Override
    public Chunk[] getLoadedChunks() {
        return new Chunk[0];
    }

    @Override
    public void loadChunk(Chunk chunk) {

    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return false;
    }

    @Override
    public void loadChunk(int x, int z) {

    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        return false;
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        return false;
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return false;
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {

    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        return null;
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        return false;
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        return false;
    }

    @Override
    public void removePluginChunkTickets(Plugin plugin) {

    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        return null;
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        return null;
    }

    @Override
    public Item dropItem(Location location, ItemStack item) {
        return null;
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item) {
        return null;
    }

    @Override
    public Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
        return null;
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location location, Vector direction, float speed, float spread, Class<T> clazz) {
        return null;
    }

    @Override
    public boolean generateTree(Location loc, TreeType type) {
        BlockPos pos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        Feature gen;
        IFeatureConfig conf = new NoFeatureConfig();

        switch (type) {
            case BIG_TREE:
                gen = TreeFeature.FANCY_TREE;
                break;
            case BIRCH:
                gen = TreeFeature.BIRCH_TREE;
                break;
            case REDWOOD:
                gen = TreeFeature.SPRUCE_TREE;
                break;
            case TALL_REDWOOD:
                gen = TreeFeature.PINE_TREE;
                break;
            case JUNGLE:
                gen = TreeFeature.MEGA_JUNGLE_TREE;
                break;
            case SMALL_JUNGLE:
                gen = TreeFeature.JUNGLE_TREE;
                break;
            case COCOA_TREE:
                gen = TreeFeature.MEGA_JUNGLE_TREE;
                break;
            case JUNGLE_BUSH:
                gen = TreeFeature.JUNGLE_GROUND_BUSH;
                break;
            case RED_MUSHROOM:
                gen = TreeFeature.HUGE_RED_MUSHROOM;
                conf = new BigMushroomFeatureConfig(true);
                break;
            case BROWN_MUSHROOM:
                gen = TreeFeature.HUGE_BROWN_MUSHROOM;
                conf = new BigMushroomFeatureConfig(true);
                break;
            case SWAMP:
                gen = TreeFeature.SWAMP_TREE;
                break;
            case ACACIA:
                gen = TreeFeature.SAVANNA_TREE;
                break;
            case DARK_OAK:
                gen = TreeFeature.DARK_OAK_TREE;
                break;
            case MEGA_REDWOOD:
                gen = TreeFeature.MEGA_PINE_TREE;
                break;
            case TALL_BIRCH:
                gen = TreeFeature.SUPER_BIRCH_TREE;
                break;
            case CHORUS_PLANT:
                ChorusFlowerBlock.generatePlant(world, pos, random, 8);
                return true;
            case TREE:
            default:
                gen = TreeFeature.NORMAL_TREE;
                break;
        }

        return gen.place(world, world.getChunkProvider().getChunkGenerator(), random, pos, conf);
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        // TODO:
        /*
        world.captureBlockSnapshots = true;
        world.restoringBlockSnapshots = true;
        boolean grownTree = generateTree(loc, type);
        world.captureBlockSnapshots = false;
        world.restoringBlockSnapshots = false;
        if (grownTree) { // Copy block data to delegate
            for (BlockSnapshot blockSnapshot : world.capturedBlockSnapshots) {
                BlockPos position = ((MagmaBlockState) blockSnapshot).getPosition();
                BlockState oldBlock = world.getBlockState(position);
                int flag = ((MagmaBlockState) blockSnapshot).getFlag();
                delegate.setBlockData(blockSnapshot.getPos().getX(), blockSnapshot.getPos().getY(), blockSnapshot.getPos().getZ(), blockSnapshot.getBlockData());
                BlockState newBlock = world.getBlockState(position);
                world.markAndNotifyBlock(position, null, oldBlock, newBlock, flag);
            }
            world.capturedBlockSnapshots.clear();
            return true;
        } else {
            world.capturedBlockSnapshots.clear();
            return false;
        }
         */
        return false;
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type) {
        return null;
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        return null;
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        return null;
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<>();

        for (net.minecraft.entity.Entity entity : world.getEntities().collect(Collectors.toList())) {
            Entity bukkitEntity = ((IBridgeEntity) entity).getBukkitEntity();

            // Assuming that bukkitEntity isn't null
            if (bukkitEntity != null && bukkitEntity.isValid()) {
                list.add(bukkitEntity);
            }
        }

        return list;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<>();

        for (net.minecraft.entity.Entity entity : world.getEntities().collect(Collectors.toList())) {
            Entity bukkitEntity = ((IBridgeEntity) entity).getBukkitEntity();

            // Assuming that bukkitEntity isn't null
            if (bukkitEntity instanceof LivingEntity && bukkitEntity.isValid()) {
                list.add((LivingEntity) bukkitEntity);
            }
        }

        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>) getEntitiesByClasses(classes);
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        Collection<T> list = new ArrayList<T>();

        for (net.minecraft.entity.Entity entity : world.getEntities().collect(Collectors.toList())) {
            Entity bukkitEntity = ((IBridgeEntity) entity).getBukkitEntity();

            if (bukkitEntity == null) {
                continue;
            }

            Class<?> bukkitClass = bukkitEntity.getClass();
            if (clazz.isAssignableFrom(bukkitClass) && bukkitEntity.isValid()) {
                list.add(clazz.cast(bukkitEntity));
            }
        }

        return list;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        Collection<Entity> list = new ArrayList<Entity>();

        for (net.minecraft.entity.Entity entity : world.getEntities().collect(Collectors.toList())) {
            Entity bukkitEntity = ((IBridgeEntity) entity).getBukkitEntity();

            if (bukkitEntity == null) {
                continue;
            }

            Class<?> bukkitClass = bukkitEntity.getClass();
            for (Class<?> clazz : classes) {
                if (clazz.isAssignableFrom(bukkitClass)) {
                    if (bukkitEntity.isValid()) {
                        list.add(bukkitEntity);
                    }
                    break;
                }
            }
        }

        return list;
    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        return getNearbyEntities(location, x, y, z, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, Predicate<Entity> filter) {
        Validate.notNull(location, "Location is null!");
        Validate.isTrue(equals(location.getWorld()), "Location is from different world!");

        BoundingBox aabb = BoundingBox.of(location, x, y, z);
        return getNearbyEntities(aabb, filter);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        return getNearbyEntities(boundingBox, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter) {
        Validate.notNull(boundingBox, "Bounding box is null!");

        List<net.minecraft.entity.Entity> entityList = world.getEntities().collect(Collectors.toList());
        List<Entity> bukkitEntityList = new ArrayList<>(entityList.size());

        for (net.minecraft.entity.Entity entity : entityList) {
            Entity bukkitEntity = ((IBridgeEntity) entity).getBukkitEntity();
            if (filter == null || filter.test(bukkitEntity)) {
                bukkitEntityList.add(bukkitEntity);
            }
        }

        return bukkitEntityList;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, Predicate<Entity> filter) {
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize, Predicate<Entity> filter) {
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        return null;
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<Entity> filter) {
        return null;
    }

    @Override
    public String getName() {
        return world.getWorldInfo().getWorldName();
    }

    @Override
    public UUID getUID() {
        return UUID.randomUUID(); //TODO: world.getSaveHandler().getUUID();
    }

    @Override
    public Location getSpawnLocation() {
        BlockPos spawn = world.getSpawnPoint();
        return new Location(this, spawn.getX(), spawn.getY(), spawn.getZ());
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        Preconditions.checkArgument(location != null, "location");

        return equals(location.getWorld()) && setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            Location previousLocation = getSpawnLocation();
            world.getWorldInfo().setSpawn(new BlockPos(x, y, z));

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            server.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long getTime() {
        long time = getFullTime() % 24000;
        if (time < 0) time += 24000;
        return time;
    }

    @Override
    public void setTime(long time) {
        long margin = (time - getFullTime()) % 24000;
        if (margin < 0) margin += 24000;
        setFullTime(getFullTime() + margin);
    }

    @Override
    public long getFullTime() {
        return world.getDayTime();
    }

    @Override
    public void setFullTime(long time) {
        world.setDayTime(time);

        // Forces the client to update to the new time immediately
        for (Player player : getPlayers()) {
            MagmaPlayer magmaPlayer = (MagmaPlayer) player;
            if (magmaPlayer.getHandle().connection == null)
                continue;

            magmaPlayer.getHandle().connection.sendPacket(new SUpdateTimePacket(getTime(), player.getPlayerTime(), world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).get()));

        }
    }

    @Override
    public boolean hasStorm() {
        return world.getWorldInfo().isRaining();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        world.getWorldInfo().setRaining(hasStorm);
        setWeatherDuration(0);
    }

    @Override
    public int getWeatherDuration() {
        return world.getWorldInfo().getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) {
        world.getWorldInfo().setRainTime(duration);
    }

    @Override
    public boolean isThundering() {
        return world.getWorldInfo().isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        world.getWorldInfo().setThundering(thundering);
        setThunderDuration(0);
    }

    @Override
    public int getThunderDuration() {
        return world.getWorldInfo().getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) {
        world.getWorldInfo().setThunderTime(duration);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return createExplosion(x, y, z, power, setFire, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(x, y, z, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, Entity source) {
        return false; // !world.createExplosion(source == null ? null : ((MagmaEntity) source).getHandle(), x, y, z, power, setFire, breakBlocks ? Explosion.Mode.BREAK : Explosion.Mode.NONE).wasCanceled;
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        return createExplosion(loc, power, false);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return createExplosion(loc, power, setFire, false);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(loc, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        Preconditions.checkArgument(loc != null, "Location is null");
        Preconditions.checkArgument(this.equals(loc.getWorld()), "Location not in world");

        return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks, source);
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public long getSeed() {
        return world.getWorldInfo().getSeed();
    }

    @Override
    public boolean getPVP() {
        return pvpEnabled;
    }

    @Override
    public void setPVP(boolean pvp) {
        this.pvpEnabled = pvp;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return chunkGenerator;
    }

    @Override
    public void save() {
        // TODO: this.server.checkSaveState();
        try {
            boolean oldSave = world.isSaveDisabled();

            world.disableLevelSaving = false;
            world.save(null, false, false);

            world.disableLevelSaving = oldSave;
        } catch (SessionLockException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return populators;
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return null;
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {

    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data) {

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data, int radius) {

    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        return null;
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setAllowedSpawnTypes(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return true; // TODO: world.getChunkProvider().allowAnimals
    }

    @Override
    public boolean getAllowMonsters() {
        return true; //TODO: world.getChunkProvider().allowMonsters;
    }

    @Override
    public Biome getBiome(int x, int z) {
        return null;
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {

    }

    @Override
    public double getTemperature(int x, int z) {
        return world.getBiome(new BlockPos(x, 0, z)).getTemperature(new BlockPos(x, 0, z));
    }

    @Override
    public double getHumidity(int x, int z) {
        return this.world.getBiome(new BlockPos(x, 0, z)).getDownfall();
    }

    @Override
    public int getMaxHeight() {
        return world.getMaxHeight();
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return keepSpawnInMemory;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        this.keepSpawnInMemory = keepLoaded;
    }

    @Override
    public boolean isAutoSave() {
        return !world.isSaveDisabled();
    }

    @Override
    public void setAutoSave(boolean value) {
        world.disableLevelSaving = !value;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        world.getWorldInfo().setDifficulty(net.minecraft.world.Difficulty.values()[difficulty.ordinal()]);
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.values()[world.getDifficulty().ordinal()];
    }

    @Override
    public File getWorldFolder() {
        return world.getSaveHandler().getWorldDirectory();
    }

    @Override
    public WorldType getWorldType() {
        return org.bukkit.WorldType.getByName(world.getWorldType().getName().toUpperCase());
    }

    @Override
    public boolean canGenerateStructures() {
        return false;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {

    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {

    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {

    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {

    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {

    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {

    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public String[] getGameRules() {
        return new String[0];
    }

    @Override
    public String getGameRuleValue(String rule) {
        return null;
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        return false;
    }

    @Override
    public boolean isGameRule(String rule) {
        return false;
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        return null;
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        return null;
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        return false;
    }

    @Override
    public WorldBorder getWorldBorder() {
        return null;
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {

    }

    @Override
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius, boolean findUnexplored) {
        BlockPos originPos = new BlockPos(origin.getX(), origin.getY(), origin.getZ());
        BlockPos nearest = world.getChunkProvider().getChunkGenerator().findNearestStructure(world, structureType.getName(), originPos, radius, findUnexplored);
        return (nearest == null) ? null : new Location(this, nearest.getX(), nearest.getY(), nearest.getZ());
    }

    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        return null;
    }

    @Override
    public List<Raid> getRaids() {
        return null;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {

    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<>();
        for (Player player : getPlayers())
            result.addAll(player.getListeningPluginChannels());
         return result;
    }
}
