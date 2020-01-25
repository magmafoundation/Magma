package org.magmafoundation.magma.mixin.core.minecraft.server.management;

import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

/**
 * MixinBanEntry
 *
 * @author Redned
 * @since 22/01/2020 - 07:21 pm
 */
@Mixin(UserList.class)
public abstract class MixinUserList<K, V extends UserListEntry<K>> {

    @Shadow @Final protected Map<String, V> values;

    @Shadow public abstract V getEntry(K obj);
    @Shadow public abstract void addEntry(V entry);
    @Shadow public abstract void removeEntry(K entry);
}
