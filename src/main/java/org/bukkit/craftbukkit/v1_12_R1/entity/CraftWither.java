package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.destroystokyo.paper.entity.CraftRangedEntity;
import net.minecraft.entity.boss.EntityWither;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.EntityType;

public class CraftWither extends CraftMonster implements Wither, CraftRangedEntity<EntityWither> { // Paper
    public CraftWither(CraftServer server, EntityWither entity) {
        super(server, entity);
    }

    @Override
    public EntityWither getHandle() {
        return (EntityWither) entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    public EntityType getType() {
        return EntityType.WITHER;
    }

}
