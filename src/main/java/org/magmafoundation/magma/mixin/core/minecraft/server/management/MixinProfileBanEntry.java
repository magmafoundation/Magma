package org.magmafoundation.magma.mixin.core.minecraft.server.management;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.management.ProfileBanEntry;

import org.bukkit.BanEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Date;

/**
 * MixinProfileBanEntry
 *
 * @author Redned
 * @since 22/01/2020 - 07:34 pm
 */
@Mixin(ProfileBanEntry.class)
public abstract class MixinProfileBanEntry extends MixinBanEntry<GameProfile> implements BanEntry {

    @NotNull
    @Override
    public String getTarget() {
        return getValue().getName();
    }

    @NotNull
    @Override
    public Date getCreated() {
        return banStartDate;
    }

    @Override
    public void setCreated(@NotNull Date created) {
        this.banStartDate = created;
    }

    @NotNull
    @Override
    public String getSource() {
        return bannedBy;
    }

    @Override
    public void setSource(@NotNull String source) {
        this.bannedBy = source;
    }

    @Nullable
    @Override
    public Date getExpiration() {
        return banEndDate;
    }

    @Override
    public void setExpiration(@Nullable Date expiration) {
        this.banEndDate = expiration;
    }

    @Nullable
    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }

    @Override
    public void save() {
        // TODO
    }
}
