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

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.FMLLog;
import org.reflections.Reflections;

/**
 * ModPatcherManager
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 01/01/2021 - 03:39 pm
 */
public class ModPatcherManager {

	private List<ModPatcher> patcherList = new ArrayList<>();

	public void init() {
		initPatches();
		FMLLog.info("%s mod patches loaded!", patcherList.size());
		patcherList.forEach(patcher -> FMLLog.info("%s loaded", patcher.getName()));
	}

	private void initPatches() {
		Reflections reflections = new Reflections(ModPatcher.class.getPackage().getName());

		reflections.getTypesAnnotatedWith(ModPatcher.ModPatcherInfo.class).forEach(aClass -> {
			try {
				ModPatcher patcher = (ModPatcher) aClass.newInstance();
				patcherList.add(patcher);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});

	}

	public List<ModPatcher> getPatcherList() {
		return patcherList;
	}
}

