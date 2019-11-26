package org.magmafoundation.magma.api.accessor.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * IAccessorEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 25/11/2019 - 03:10 pm
 */
@Mixin(Entity.class)
public interface IAccessorEntity {

    @Accessor(value = "getFlag")
    boolean getFlag(int flag);

    @Accessor(value = "setFlag")
    void setFlag(int flag, boolean set);

}
