package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.destroystokyo.paper.entity.SentientNPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityVex;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vex;

public class CraftVex extends CraftMonster implements Vex {

    public CraftVex(CraftServer server, EntityVex entity) {
        super(server, entity);
    }

    @Override
    public EntityVex getHandle() {
        return (EntityVex) super.getHandle();
    }

    // Paper start
    public SentientNPC getOwner() {
        EntityLiving owner = getHandle().getOwner();
        return owner != null ? (SentientNPC) owner.getBukkitEntity() : null;
    }
    // Paper end

    @Override
    public String toString() {
        return "CraftVex";
    }

    @Override
    public EntityType getType() {
        return EntityType.VEX;
    }
}
