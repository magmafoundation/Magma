package org.magmafoundation.magma.mixin.core.minecraft.server.management;

import net.minecraft.server.management.UserListEntry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinIPBanEntry
 *
 * @author Redned
 * @since 22/01/2020 - 07:18 pm
 */
@Mixin(UserListEntry.class)
public abstract class MixinUserListEntry<T> {

    @Shadow abstract T getValue();
}
