package org.magmafoundation.magma.api.mixin.server.management;

import javax.annotation.Nullable;
import net.minecraft.server.management.UserListEntry;
import org.magmafoundation.magma.api.bridge.server.management.IBridgeUserListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinUserListEntry
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 06:50 pm
 */
@Mixin(UserListEntry.class)
public class MixinUserListEntry<T> implements IBridgeUserListEntry<T> {

    @Shadow
    @Final
    @Nullable
    private T value;

    @Override
    public T getValue() {
        return value;
    }
}
