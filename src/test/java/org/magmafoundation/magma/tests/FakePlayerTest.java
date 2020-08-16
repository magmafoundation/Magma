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
