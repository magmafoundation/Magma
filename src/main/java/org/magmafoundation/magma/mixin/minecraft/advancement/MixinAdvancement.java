package org.magmafoundation.magma.mixin.minecraft.advancement;

import net.minecraft.advancements.Criterion;
import net.minecraft.util.ResourceLocation;

import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Map;

/**
 * MixinAdvancement
 *
 * @author Redned
 * @since 16/01/2019 - 07:01 am
 */
@Mixin(net.minecraft.advancements.Advancement.class)
@Implements(@Interface(iface = Advancement.class, prefix = "advancement$"))
public abstract class MixinAdvancement implements Advancement {

    @Shadow @Final private ResourceLocation id;
    @Shadow public abstract Map<String, Criterion> shadow$getCriteria();

    @Override
    public Collection<String> getCriteria() {
        return shadow$getCriteria().keySet();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(id.getNamespace(), id.getPath());
    }
}
