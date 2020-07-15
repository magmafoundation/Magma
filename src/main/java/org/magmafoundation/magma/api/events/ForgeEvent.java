package org.magmafoundation.magma.api.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

/**
 * ForgeEvents
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 06/03/2020 - 12:32 am
 */
public class ForgeEvent extends org.bukkit.event.Event {

    private static final HandlerList handlers = new HandlerList();
    private final Event forgeEvent;

    public ForgeEvent(Event event) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.forgeEvent = event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Event getForgeEvent() {
        return forgeEvent;
    }

    public String getEventName() {
        return forgeEvent.getClass().getSimpleName();
    }
}
