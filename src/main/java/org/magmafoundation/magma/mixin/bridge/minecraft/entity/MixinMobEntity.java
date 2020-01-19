package org.magmafoundation.magma.mixin.bridge.minecraft.entity;

import net.minecraft.entity.MobEntity;
import org.magmafoundation.magma.bridge.minecraft.entity.BridgeMobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public class MixinMobEntity implements BridgeMobEntity {

    @Shadow private boolean persistenceRequired;

    @Override
    public boolean bridge$isPersistenceRequired() {
        return persistenceRequired;
    }

    @Override
    public void bridge$setPersistenceRequired(boolean persistenceRequired) {
        this.persistenceRequired = persistenceRequired;
    }
}
