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

package org.magmafoundation.magma.inventory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

/**
 * ForgeItemCap
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 02/01/2021 - 12:58 pm
 */
public class ForgeItemCap implements Cloneable {

	private static final Logger logger = Logger.getLogger("ForgeItemCap");
	protected NBTTagCompound forgeCap;

	public ForgeItemCap(NBTTagCompound forgeCap) {
		this.forgeCap = forgeCap;
	}

	public static void setForgeItemCap(ItemStack nmsItemStack, org.bukkit.inventory.ItemStack bukkitItemStack) {
		if (nmsItemStack != null && nmsItemStack.capabilities != null) {
			NBTTagCompound nbtCap = nmsItemStack.capabilities.serializeNBT();
			if (nbtCap != null && !nbtCap.hasNoTags()) {
				bukkitItemStack.setForgeItemCap(new ForgeItemCap(nbtCap));
			}
		}
	}

	public static ForgeItemCap deserializeNBT(String serializedNBT) {
		if (serializedNBT != null) {
			ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64(serializedNBT));
			try {
				NBTTagCompound nbtCap = CompressedStreamTools.readCompressed(buf);
				return new ForgeItemCap(nbtCap);
			} catch (IOException e) {
				logger.log(Level.SEVERE, null, e);
			}
		}
		return null;
	}

	public NBTTagCompound getForgeCap() {
		return forgeCap;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ForgeItemCap)) {
			return false;
		}
		return this.forgeCap.equals(((ForgeItemCap) o).forgeCap);
	}

	@Override
	public int hashCode() {
		return this.forgeCap.hashCode();
	}

	@Override
	public ForgeItemCap clone() {
		try {
			return (ForgeItemCap) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	public String serializeNBT() {
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			CompressedStreamTools.writeCompressed(forgeCap, buf);
			return Base64.encodeBase64String(buf.toByteArray());
		} catch (IOException e) {
			logger.log(Level.SEVERE, null, e);
		}
		return null;
	}
}
