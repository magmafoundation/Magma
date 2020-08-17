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

package org.magmafoundation.magma.tests;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.magmafoundation.magma.entity.CraftFakePlayer;

/**
 * FakePlayerTest
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 16/08/2020 - 02:25 pm
 */
public class FakePlayerTest extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FakePlayerListener(), this);
    }

    static class FakePlayerListener implements Listener {

        @EventHandler(ignoreCancelled = true)
        public void wayOne(BlockBreakEvent event) {
            if(event.getPlayer() instanceof CraftFakePlayer){
                CraftFakePlayer player = (CraftFakePlayer) event.getPlayer();
                System.out.println(player.getName());
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void wayTwo(BlockBreakEvent event) {
            if(event.getPlayer().isFakePlayer()){
                CraftFakePlayer player = (CraftFakePlayer) event.getPlayer();
                System.out.println(player.getName());
            }
        }
    }
}
