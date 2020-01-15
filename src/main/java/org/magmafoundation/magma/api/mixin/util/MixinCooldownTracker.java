package org.magmafoundation.magma.api.mixin.util;

import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.util.CooldownTracker;
import org.magmafoundation.magma.api.bridge.util.IBridgeCooldownTracker;
import org.magmafoundation.magma.api.core.util.Cooldown;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinCooldownTracker
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 08:54 am
 */
@Mixin(CooldownTracker.class)
public class MixinCooldownTracker implements IBridgeCooldownTracker {

    //@formatter:off
    @Shadow @Final private Map<Item, Cooldown> cooldowns;
    @Shadow private int ticks;
    //@formatter:on

    @Override
    public Map<Item, Cooldown> getCooldowns() {
        return cooldowns;
    }

    @Override
    public int getCurrentTick() {
        return ticks;
    }


}
