package org.magmafoundation.magma.api.bridge.util;

import java.util.Map;
import net.minecraft.item.Item;
import org.magmafoundation.magma.api.core.util.Cooldown;

/**
 * IBridgeCooldownTracker
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/11/2019 - 08:55 am
 */
public interface IBridgeCooldownTracker {

    Map<Item, Cooldown> getCooldowns();

    int getCurrentTick();

}
