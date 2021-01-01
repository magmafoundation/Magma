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

package org.magmafoundation.magma.modPatcher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * ModPatcher
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 01/01/2021 - 03:31 pm
 */
public abstract class ModPatcher implements IClassTransformer {

	private String name = getClass().getAnnotation(ModPatcherInfo.class).name();

	public abstract byte[] transform(String name, String transformedName, byte[] clazz);

	public String getName() {
		return name;
	}


	@Retention(RetentionPolicy.RUNTIME)
	public @interface ModPatcherInfo {

		String name();

	}

}


