package org.magmafoundation.magma.api.mixin.server.management;

import java.util.Date;
import net.minecraft.server.management.BanEntry;
import org.magmafoundation.magma.api.bridge.server.management.IBridgeBanEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinBanEntry
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 04:46 am
 */
@Mixin(BanEntry.class)
public class MixinBanEntry implements IBridgeBanEntry {


    @Shadow
    @Final
    protected Date banStartDate;

    @Override
    public Date getBanStartDate() {
        return banStartDate;
    }
}
