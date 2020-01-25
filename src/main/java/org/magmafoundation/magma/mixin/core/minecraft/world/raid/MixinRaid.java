package org.magmafoundation.magma.mixin.core.minecraft.world.raid;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.entity.Raider;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

/**
 * MixinRaid
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/01/2020 - 10:30 am
 */

@Mixin(net.minecraft.world.raid.Raid.class)
@Implements(@Interface(iface = Raid.class, prefix = "raid$"))
public abstract class MixinRaid implements Raid {

    //@formatter:off
    @Shadow private boolean started;
    @Shadow private long ticksActive;
    @Shadow private int badOmenLevel;
    @Shadow public abstract int getMaxLevel();
    @Shadow private BlockPos center;
    @Shadow @Final private ServerWorld world;
    @Shadow public abstract boolean isStopped();
    @Shadow public abstract boolean isVictory();
    @Shadow public abstract boolean isLoss();
    @Shadow @Final private int numGroups;
    @Shadow private float totalHealth;
    @Shadow public abstract int func_221315_l();
    @Shadow @Final private Set<UUID> heroes;
    //@formatter:on

    @Intrinsic
    public boolean isStarted() {
        return started;
    }

    @Override
    public long getActiveTicks() {
        return ticksActive;
    }

    @Override
    public int getBadOmenLevel() {
        return badOmenLevel;
    }

    @Override
    public void setBadOmenLevel(int badOmenLevel) {
        int max = getMaxLevel();
        Preconditions.checkArgument(0 <= badOmenLevel && badOmenLevel <= max, "Bad Omen level must be between 0 and %s", max);
        this.badOmenLevel = badOmenLevel;
    }

    @NotNull
    @Override
    public Location getLocation() {
        return new Location((org.bukkit.World) world, center.getX(), center.getY(), center.getZ());
    }

    @NotNull
    @Override
    public RaidStatus getStatus() {
        if (isStopped()) {
            return RaidStatus.STOPPED;
        } else if (isVictory()) {
            return RaidStatus.VICTORY;
        } else if (isLoss()) {
            return RaidStatus.LOSS;
        } else {
            return RaidStatus.ONGOING;
        }
    }

    @Override
    public int getSpawnedGroups() {
        return func_221315_l();
    }

    @Override
    public int getTotalGroups() {
        return numGroups + (badOmenLevel > 1 ? 1 : 0);
    }

    @Override
    public int getTotalWaves() {
        return numGroups;
    }

    @Override
    public float getTotalHealth() {
        return totalHealth;
    }

    @NotNull
    @Override
    public Set<UUID> getHeroes() {
        return Collections.unmodifiableSet(heroes);
    }

    @NotNull
    @Override
    public List<Raider> getRaiders() {
        return null;// TODO: 25/01/2020  
    }


}
