package org.magmafoundation.magma.api.accessor;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * IAccessorLivingEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 05:20 pm
 */
@Mixin(LivingEntity.class)
public interface IAccessorLivingEntity {

    @Accessor(value = "")

}
