package org.magmafoundation.magma.api.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.magmafoundation.magma.api.bridge.IBridgeEntity;
import org.magmafoundation.magma.api.core.MagmaServer;
import org.magmafoundation.magma.api.core.entity.MagmaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * MixinEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 24/11/2019 - 08:19 pm
 */
@Mixin(Entity.class)
public class MixinEntity implements IBridgeEntity {

    @Shadow
    public float rotationYaw;

    protected MagmaEntity magmaEntity;

    @Override
    public float getBukkitYaw() {
        return rotationYaw;
    }


    @Override
    public MagmaEntity getBukkitEntity() {
        if (magmaEntity == null) {
            magmaEntity = MagmaEntity
                .getEntity((MagmaServer) Bukkit.getServer(), (Entity) (Object) this);
        }
        return magmaEntity;
    }
}
