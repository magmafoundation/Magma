package com.destroystokyo.paper.entity;

import net.minecraft.entity.EntityLiving;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public interface CraftSentientNPC<T extends EntityLiving> extends SentientNPC {

    T getHandle();

    default public void setTarget(LivingEntity target) {
        T entity = getHandle();
        if (target == null) {
            entity.setAttackTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setAttackTarget(((CraftLivingEntity) target).getHandle(), null, false);
        }
    }

    default public LivingEntity getTarget() {
        if (getHandle().getAttackTarget() == null) {
            return null;
        }
        return (CraftLivingEntity) getHandle().getAttackTarget().getBukkitEntity();
    }
}
