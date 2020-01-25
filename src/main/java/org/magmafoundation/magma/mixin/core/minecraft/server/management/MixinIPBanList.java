package org.magmafoundation.magma.mixin.core.minecraft.server.management;

import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.IPBanList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.Date;
import java.util.Set;

/**
 * MixinIPBanList
 *
 * @author Redned
 * @since 22/01/2020 - 06:51 pm
 */
@Mixin(IPBanList.class)
@Implements(@Interface(iface = BanList.class, prefix = "banlist$"))
public abstract class MixinIPBanList extends MixinUserList<String, IPBanEntry> implements BanList {

    @Shadow public abstract boolean shadow$isBanned(String p_199044_1_);

    @Nullable
    @Override
    public BanEntry getBanEntry(@NotNull String target) {
        return (BanEntry) getEntry(target);
    }

    @Nullable
    @Override
    public BanEntry addBan(@NotNull String target, @Nullable String reason, @Nullable Date expires, @Nullable String source) {
        IPBanEntry ipBanEntry = new IPBanEntry(target, new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);

        addEntry(ipBanEntry);
        return (BanEntry) ipBanEntry;
    }

    @NotNull
    @Override
    public Set<BanEntry> getBanEntries() {
        return (Set<BanEntry>) (Object) values.keySet();
    }

    @Intrinsic
    public boolean banlist$isBanned(@NotNull String target) {
        return shadow$isBanned(target);
    }

    @Override
    public void pardon(@NotNull String target) {
        removeEntry(target);
    }
}
