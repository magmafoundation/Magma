package org.bukkit.entity;

import com.destroystokyo.paper.entity.SentientNPC;

/**
 * Represents a Vex.
 */
public interface Vex extends Monster {
    /**
     * @return What Entity (most likely an Evoker, but not guaranteed) summoned this Vex
     */
    SentientNPC getOwner(); // Paper
}
