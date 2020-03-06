package org.magmafoundation.magma.api.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * ForgeEvents
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 06/03/2020 - 12:32 am
 */
public class ForgeEvents extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final net.minecraftforge.fml.common.eventhandler.Event forgeEvents;

    public ForgeEvents(net.minecraftforge.fml.common.eventhandler.Event forgeEvents) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.forgeEvents = forgeEvents;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public net.minecraftforge.fml.common.eventhandler.Event getForgeEvents() {
        return forgeEvents;
    }

    public static HandlerList getHandlersList() {
        return handlers;
    }
}
