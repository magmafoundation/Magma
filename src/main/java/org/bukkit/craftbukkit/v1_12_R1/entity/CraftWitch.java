package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.monster.EntityWitch;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Witch;
import org.bukkit.entity.EntityType;

public class CraftWitch extends CraftMonster implements Witch {
    public CraftWitch(CraftServer server, EntityWitch entity) {
        super(server, entity);
    }

    @Override
    public EntityWitch getHandle() {
        return (EntityWitch) entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    public EntityType getType() {
        return EntityType.WITCH;
    }
}
