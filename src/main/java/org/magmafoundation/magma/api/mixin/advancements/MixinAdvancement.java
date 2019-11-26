package org.magmafoundation.magma.api.mixin.advancements;

import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.util.ResourceLocation;
import org.magmafoundation.magma.api.bridge.advancements.IMixinAdvancement;
import org.magmafoundation.magma.api.core.advancement.MagmaAdvancement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Advancement.class)
public class MixinAdvancement implements IMixinAdvancement {

    MagmaAdvancement bukkit;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(ResourceLocation id, Advancement parentIn, DisplayInfo displayIn,
        AdvancementRewards rewardsIn, Map<String, Criterion> criteriaIn, String[][] requirementsIn,
        CallbackInfo ci) {
        bukkit = new MagmaAdvancement((Advancement) (Object) this);

    }

    public org.bukkit.advancement.Advancement getBukkitAdvancements() {
        return bukkit;
    }
}
