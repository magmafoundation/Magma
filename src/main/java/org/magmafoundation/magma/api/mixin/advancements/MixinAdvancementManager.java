package org.magmafoundation.magma.api.mixin.advancements;

import com.google.gson.Gson;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import org.magmafoundation.magma.api.bridge.advancements.IAdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinAdvancementManager
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 26/11/2019 - 08:41 am
 */
@Mixin(AdvancementManager.class)
public class MixinAdvancementManager implements IAdvancementManager {

    @Shadow
    @Final
    private static Gson GSON;

    @Shadow
    private AdvancementList field_223388_c;

    @Override
    public Gson getGson() {
        return GSON;
    }

    @Override
    public AdvancementList getAdvancementList() {
        return field_223388_c;
    }
}
