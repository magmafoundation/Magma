package org.magmafoundation.magma.api.core;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.SaveHandler;
import org.bukkit.BanList.Type;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.magmafoundation.magma.api.bridge.server.dedicated.IBridgeDedicatedPlayerList;
import org.magmafoundation.magma.api.bridge.server.IBridgeMinecraftServer;
import org.magmafoundation.magma.api.bridge.server.management.IBridgeSaveHandler;

/**
 * MagmaOfflinePlayer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 07:04 pm
 */
public class MagmaOfflinePlayer implements OfflinePlayer, ConfigurationSerializable {

    private final GameProfile profile;
    private final MagmaServer server;
    private final SaveHandler storage;

    public MagmaOfflinePlayer(GameProfile profile, MagmaServer server) {
        this.profile = profile;
        this.server = server;
        this.storage = (SaveHandler) ((IBridgeMinecraftServer) server.dedicatedServer)
            .getServerWorldList().get(0).getSaveHandler();
    }

    public GameProfile getProfile() {
        return profile;
    }

    @Override
    public boolean isOnline() {
        return getPlayer() != null;
    }

    @Override
    public String getName() {
        Player player = getPlayer();
        if (player != null) {
            return player.getName();
        }

        // Last known Name
        if (profile.getName() != null) {
            return profile.getName();
        }

        CompoundNBT data = getNBTData();

        if (data != null) {
            if (data.contains("lastKnownName")) {
                return data.getString("lastKnownName");
            }
        }

        return null;
    }

    private CompoundNBT getNBTData() {
        CompoundNBT result = getData();

        if (result != null) {
            if (!result.contains("bukkit")) {
                result.put("bukkit", new CompoundNBT());
            }
            result = result.getCompound("bukkit");
        }

        return result;
    }

    private CompoundNBT getData() {
        return ((IBridgeSaveHandler) storage).getPlayerNBT(getUniqueId().toString());
    }

    @Override
    public UUID getUniqueId() {
        return profile.getId();
    }

    @Override
    public boolean isBanned() {
        if (getName() == null) {
            return false;
        }
        return server.getBanList(Type.NAME).isBanned(getName());
    }

    @Override
    public boolean isWhitelisted() {
        return server.getDedicatedPlayerList().getWhitelistedPlayers().isWhitelisted(profile);
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            ((IBridgeDedicatedPlayerList) server.getDedicatedPlayerList())
                .addPlayerToWhitelist(profile);
        } else {
            ((IBridgeDedicatedPlayerList) server.getDedicatedPlayerList())
                .removePlayerFromWhitelist(profile);
        }
    }

    @Override
    public Player getPlayer() {
        return server.getPlayer(getUniqueId());
    }

    @Override
    public long getFirstPlayed() {
        Player player = getPlayer();
        if (player != null) {
            return player.getFirstPlayed();
        }

        CompoundNBT data = getNBTData();

        if (data != null) {
            if (data.contains("firstPlayed")) {
                return data.getLong("firstPlayed");
            } else {
                File file = getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    @Override
    public long getLastPlayed() {
        Player player = getPlayer();
        if (player != null) {
            return player.getLastPlayed();
        }

        CompoundNBT data = getNBTData();

        if (data != null) {
            if (data.contains("lastPlayed")) {
                return data.getLong("lastPlayed");
            } else {
                File file = getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    private File getDataFile() {
        return new File(storage.getPlayerFolder(), getUniqueId() + ".dat");
    }

    @Override
    public boolean hasPlayedBefore() {
        return getData() != null;
    }

    @Override
    public Location getBedSpawnLocation() {
        CompoundNBT data = getData();
        if (data == null) {
            return null;
        }

        if (data.contains("SpawnX") && data.contains("SpawnY") && data.contains("SpawnZ")) {
            String worldSpawn = data.getString("SpawnWorld");
            if (worldSpawn.equals("")) {
                worldSpawn = server.getWorlds().get(0).getName();
            }
            return new Location(server.getWorld(worldSpawn), data.getInt("SpawnX"),
                data.getInt("SpawnY"), data.getInt("SpawnZ"));
        }
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("UUID", profile.getId().toString());
        return result;
    }

    @Override
    public boolean isOp() {
        return server.getDedicatedPlayerList().canSendCommands(profile);
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) {
            return;
        }

        if (value) {
            server.getDedicatedPlayerList().addOp(profile);
        } else {
            server.getDedicatedPlayerList().removeOp(profile);
        }
    }

}
