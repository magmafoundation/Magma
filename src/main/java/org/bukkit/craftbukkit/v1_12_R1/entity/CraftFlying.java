package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.destroystokyo.paper.entity.CraftSentientNPC;
import net.minecraft.entity.EntityFlying;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Flying;

public class CraftFlying extends CraftLivingEntity implements Flying, CraftSentientNPC { // Paper

    public CraftFlying(CraftServer server, EntityFlying entity) {
        super(server, entity);
    }

    @Override
    public EntityFlying getHandle() {
        return (EntityFlying) entity;
    }

    @Override
    public String toString() {
        return "CraftFlying";
    }
}
