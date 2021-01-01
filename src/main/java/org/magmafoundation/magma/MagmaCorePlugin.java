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

package org.magmafoundation.magma;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.magmafoundation.magma.modPatcher.ModPatcherManager;

/**
 * MagmaCorePlugin
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 01/01/2021 - 03:25 pm
 */
public class MagmaCorePlugin implements IFMLLoadingPlugin {

	public MagmaCorePlugin() {
		Magma.getInstance().setModPatcherManager(new ModPatcherManager());
		Magma.getInstance().getModPatcherManager().init();
	}

	@Override
	public String[] getASMTransformerClass() {
		ArrayList<String> modPatchClass = new ArrayList<>();
		Magma.getInstance().getModPatcherManager().getPatcherList().forEach(modPatcher -> modPatchClass.add(modPatcher.getClass().getCanonicalName()));
		String[] transformClass = new String[modPatchClass.size()];
		return modPatchClass.toArray(transformClass);
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
