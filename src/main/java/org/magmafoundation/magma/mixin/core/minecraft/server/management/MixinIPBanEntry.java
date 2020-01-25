package org.magmafoundation.magma.mixin.core.minecraft.server.management;

import net.minecraft.server.management.IPBanEntry;

import org.bukkit.BanEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Date;

/**
 * MixinIPBanEntry
 *
 * @author Redned
 * @since 22/01/2020 - 06:51 pm
 */
@Mixin(IPBanEntry.class)
public abstract class MixinIPBanEntry extends MixinBanEntry<String> implements BanEntry {

    @NotNull
    @Override
    public String getTarget() {
        return getValue();
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
