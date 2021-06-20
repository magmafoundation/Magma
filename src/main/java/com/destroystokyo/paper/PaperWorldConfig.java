package com.destroystokyo.paper;

import static com.destroystokyo.paper.PaperConfig.log;

import java.util.Arrays;
import java.util.List;

import com.destroystokyo.paper.antixray.ChunkPacketBlockControllerAntiXray.ChunkEdgeMode;
import com.destroystokyo.paper.antixray.ChunkPacketBlockControllerAntiXray.EngineMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spigotmc.SpigotWorldConfig;

public class PaperWorldConfig {

	private final String worldName;
	private final SpigotWorldConfig spigotConfig;
	private final YamlConfiguration config;
	private boolean verbose;

	public PaperWorldConfig(String worldName, SpigotWorldConfig spigotConfig) {
		this.worldName = worldName;
		this.spigotConfig = spigotConfig;
		this.config = PaperConfig.config;
		init();
	}

	public void init() {
		log("-------- World Settings For [" + worldName + "] --------");
		PaperConfig.readConfig(PaperWorldConfig.class, this);
	}

	private void set(String path, Object val) {
		config.set("world-settings.default." + path, val);
		if (config.get("world-settings." + worldName + "." + path) != null) {
			config.set("world-settings." + worldName + "." + path, val);
		}
	}

	private boolean getBoolean(String path, boolean def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getBoolean("world-settings." + worldName + "." + path, config.getBoolean("world-settings.default." + path));
	}

	private double getDouble(String path, double def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getDouble("world-settings." + worldName + "." + path, config.getDouble("world-settings.default." + path));
	}

	private int getInt(String path, int def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getInt("world-settings." + worldName + "." + path, config.getInt("world-settings.default." + path));
	}

	private float getFloat(String path, float def) {
		// TODO: Figure out why getFloat() always returns the default value.
		return (float) getDouble(path, (double) def);
	}

	private <T> List getList(String path, T def) {
		config.addDefault("world-settings.default." + path, def);
		return (List<T>) config.getList("world-settings." + worldName + "." + path, config.getList("world-settings.default." + path));
	}

	private String getString(String path, String def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getString("world-settings." + worldName + "." + path, config.getString("world-settings.default." + path));
	}

	public boolean antiXray;
	public boolean asynchronous;
	public EngineMode engineMode;
	public ChunkEdgeMode chunkEdgeMode;
	public int maxChunkSectionIndex;
	public int updateRadius;
	public List<Object> hiddenBlocks;
	public List<Object> replacementBlocks;

	private void antiXray() {
		antiXray = getBoolean("anti-xray.enabled", false);
		asynchronous = true;
		engineMode = EngineMode.getById(getInt("anti-xray.engine-mode", EngineMode.HIDE.getId()));
		engineMode = engineMode == null ? EngineMode.HIDE : engineMode;
		chunkEdgeMode = ChunkEdgeMode.getById(getInt("anti-xray.chunk-edge-mode", ChunkEdgeMode.DEFAULT.getId()));
		chunkEdgeMode = chunkEdgeMode == null ? ChunkEdgeMode.DEFAULT : chunkEdgeMode;
		maxChunkSectionIndex = getInt("anti-xray.max-chunk-section-index", 3);
		maxChunkSectionIndex = maxChunkSectionIndex > 15 ? 15 : maxChunkSectionIndex;
		updateRadius = getInt("anti-xray.update-radius", 2);
		hiddenBlocks = getList("anti-xray.hidden-blocks", Arrays.asList((Object) "gold_ore", "iron_ore", "coal_ore", "lapis_ore", "mossy_cobblestone", "obsidian", "chest", "diamond_ore", "redstone_ore", "lit_redstone_ore", "clay", "emerald_ore", "ender_chest"));
		replacementBlocks = getList("anti-xray.replacement-blocks", Arrays.asList((Object) "stone", "planks"));
		log("Anti-Xray: " + (antiXray ? "enabled" : "disabled") + " / Engine Mode: " + engineMode.getDescription() + " / Chunk Edge Mode: " + chunkEdgeMode.getDescription() + " / Up to " + ((maxChunkSectionIndex + 1) * 16) + " blocks / Update Radius: " + updateRadius);
	}

}
