package org.magmafoundation.magma.api.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import org.magmafoundation.magma.api.bridge.IBridgeDedicatedPlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinDedicatedPlayerList
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 07:31 pm
 */
@Mixin(DedicatedPlayerList.class)
public abstract class MixinDedicatedPlayerList extends MixinPlayerList implements
    IBridgeDedicatedPlayerList {

    @Shadow
    protected abstract void saveWhiteList();

    @Override
    public void removePlayerFromWhitelist(GameProfile profile) {
        super.removePlayerFromWhitelist(profile);
        this.saveWhiteList();
    }

    @Override
    public void addPlayerToWhitelist(GameProfile profile) {
        super.addPlayerToWhitelist(profile);
        this.saveWhiteList();
    }
}
