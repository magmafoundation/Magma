package org.magmafoundation.magma.mixin.core.minecraft.server.management;

import net.minecraft.server.management.BanEntry;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Date;

/**
 * MixinBanEntry
 *
 * @author Redned
 * @since 22/01/2020 - 06:52 pm
 */
@Mixin(BanEntry.class)
public abstract class MixinBanEntry<T> extends MixinUserListEntry<T> {

    @Shadow @Final protected Date banStartDate;
    @Shadow @Final protected String bannedBy;
    @Shadow @Final protected Date banEndDate;
    @Shadow @Final protected String reason;
}
