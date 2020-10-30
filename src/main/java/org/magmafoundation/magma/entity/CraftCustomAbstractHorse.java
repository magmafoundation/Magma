/*
 * Magma Server
 * Copyright (C) 2019-2020.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.magmafoundation.magma.entity;

import net.minecraft.entity.passive.AbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;

/**
 * CraftCustomAbstractHorse
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 30/10/2020 - 02:38 am
 */
public class CraftCustomAbstractHorse extends CraftAbstractHorse {

    public CraftCustomAbstractHorse(CraftServer server, AbstractHorse entity) {
        super(server, entity);
    }

    @Override
    public Variant getVariant() {
        return Variant.FORGE_HORSE_CHESTED;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_HORSE;
        }
    }

    @Override
    public String toString() {
        return "CraftCustomAbstractHorse";
    }
}
