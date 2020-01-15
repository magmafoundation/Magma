package org.magmafoundation.magma.api.bridge.advancements;

import com.google.gson.Gson;
import net.minecraft.advancements.AdvancementList;

/**
 * IAdvancementManager
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 26/11/2019 - 08:42 am
 */
public interface IAdvancementManager {

    Gson getGson();

    AdvancementList getAdvancementList();
}
