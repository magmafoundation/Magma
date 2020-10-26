package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.destroystokyo.paper.entity.CraftSentientNPC;
import net.minecraft.entity.EntityLiving;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity, CraftSentientNPC { // Paper
    public CraftComplexLivingEntity(CraftServer server, EntityLiving entity) {
        super(server, entity);
    }

    @Override
    public EntityLiving getHandle() {
        return (EntityLiving) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
