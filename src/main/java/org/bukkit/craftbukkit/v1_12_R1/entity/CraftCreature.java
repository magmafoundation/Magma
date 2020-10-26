package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.destroystokyo.paper.entity.CraftSentientNPC;
import net.minecraft.entity.EntityCreature;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class CraftCreature extends CraftLivingEntity implements Creature, CraftSentientNPC { // Paper
    public CraftCreature(CraftServer server, EntityCreature entity) {
        super(server, entity);
    }

    // Paper start - move down to SentientNPC
    /*public void setTarget(LivingEntity target) {
        EntityCreature entity = getHandle();
        if (target == null) {
            entity.setAttackTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setAttackTarget(((CraftLivingEntity) target).getHandle(), null, false);
        }
    }

    public CraftLivingEntity getTarget() {
        if (getHandle().getAttackTarget() == null) return null;

        return (CraftLivingEntity) getHandle().getAttackTarget().getBukkitEntity();
    }*/
    // Paper end
    @Override
    public EntityCreature getHandle() {
        return (EntityCreature) entity;
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }
}
