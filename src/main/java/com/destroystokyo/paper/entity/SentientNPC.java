/*
 * Copyright (c) 2018 Daniel Ennis (Aikar) MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.destroystokyo.paper.entity;

import org.bukkit.entity.LivingEntity;

/**
 * Used to determine ACTUAL Living NPC's. Spigot mistakenly inversed the conditions for LivingEntity, and
 * used LivingEntity for Insentient Entities, and named the actual EntityLiving class EntityInsentient.
 *
 * This should of all been inversed on the implementation side. To make matters worse, Spigot never
 * exposed the differentiator that there are entities with AI that are not sentient/alive such as
 * Armor stands and Players are the only things that do not implement the REAL EntityLiving class (named Insentient internally)
 *
 * This interface lets you identify NPC entities capable of sentience, and able to move about and react to the world.
 */
public interface SentientNPC extends LivingEntity {

    /**
     * Instructs this Creature to set the specified LivingEntity as its
     * target.
     * <p>
     * Hostile creatures may attack their target, and friendly creatures may
     * follow their target.
     *
     * @param target New LivingEntity to target, or null to clear the target
     */
    public void setTarget(LivingEntity target);

    /**
     * Gets the current target of this Creature
     *
     * @return Current target of this creature, or null if none exists
     */
    public LivingEntity getTarget();
}
