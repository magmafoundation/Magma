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

package org.magmafoundation.magma.utils;

import net.minecraft.util.ResourceLocation;

/**
 * KeyedObject
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 11/07/2020 - 05:04 pm
 */
public interface KeyedObject {

    ResourceLocation getMinecraftKey();

    default String getMinecraftKeyString() {
        return getMinecraftKey().toString();
    }
}
