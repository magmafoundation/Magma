package org.bukkit.entity;

/**
 * Represents a Creature. Creatures are non-intelligent monsters or animals
 * which have very simple abilities.
 */
public interface Creature extends LivingEntity, com.destroystokyo.paper.entity.SentientNPC { // Paper

    /**
     * Instructs this Creature to set the specified LivingEntity as its
     * target.
     * <p>
     * Hostile creatures may attack their target, and friendly creatures may
     * follow their target.
     *
     * @param target New LivingEntity to target, or null to clear the target
     */
    //public void setTarget(LivingEntity target);  // Paper - moved to SentientNPC

    /**
     * Gets the current target of this Creature
     *
     * @return Current target of this creature, or null if none exists
     */
    //public LivingEntity getTarget(); // Paper - moved to SentientNPC
}
