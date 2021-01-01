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

package org.magmafoundation.magma.modPatcher.utils;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;

/**
 * ModPatchUtil
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 01/01/2021 - 05:16 pm
 */
public class ModPatchUtil {

	public static void patchedHealthUpdate(EntityPlayer player, DataParameter key, Object value) {
		if (key.equals(EntityPlayer.HEALTH)) {
			float health = (float) value;
			if (player instanceof EntityPlayerMP) {
				final CraftPlayer craftPlayer = ((EntityPlayerMP) player).getBukkitEntity();
				if (health < 0.0f) {
					craftPlayer.setRealHealth(0.0f);
				} else if (health > craftPlayer.getMaxHealth()) {
					craftPlayer.setRealHealth(craftPlayer.getMaxHealth());
				} else {
					craftPlayer.setRealHealth(health);
				}
			}
		}
	}
}
