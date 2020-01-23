package org.magmafoundation.magma.mixin.core.minecraft.server.management;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.ProfileBanEntry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * MixinBanEntry
 *
 * @author Redned
 * @since 22/01/2020 - 07:39 pm
 */
@Mixin(net.minecraft.server.management.BanList.class)
public abstract class MixinBanList extends MixinUserList<GameProfile, ProfileBanEntry> implements BanList {

    @Shadow public abstract boolean isBanned(GameProfile profile);

    @Nullable
    @Override
    public BanEntry getBanEntry(@NotNull String target) {
        return (BanEntry) getEntry(getProfile(target));
    }

    @Nullable
    @Override
    public BanEntry addBan(@NotNull String target, @Nullable String reason, @Nullable Date expires, @Nullable String source) {
        ProfileBanEntry profileBanEntry = new ProfileBanEntry(getProfile(target), new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);
        addEntry(profileBanEntry);
        return (BanEntry) profileBanEntry;
    }

    @NotNull
    @Override
    public Set<BanEntry> getBanEntries() {
        return (Set<BanEntry>) (Object) values.keySet();
    }

    @Override
    public boolean isBanned(@NotNull String target) {
        return isBanned(getProfile(target));
    }

    @Override
    public void pardon(@NotNull String target) {
        removeEntry(getProfile(target));
    }

    private GameProfile getProfile(String target) {
        try {
            return ((ServerPlayerEntity) Bukkit.getServer().getPlayer(UUID.fromString(target))).getGameProfile();
        } catch (IllegalArgumentException ex) {
            return ((ServerPlayerEntity) Bukkit.getServer().getPlayer(target)).getGameProfile();
        }
    }
}
