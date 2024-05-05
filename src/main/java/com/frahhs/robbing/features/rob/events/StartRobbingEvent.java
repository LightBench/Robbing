package com.frahhs.robbing.features.rob.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents an event indicating the start of a robbing action between two players.
 */
public class StartRobbingEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private final Player robber;
    private final Player robbed;

    /**
     * Constructs a new StartRobbingEvent.
     *
     * @param robber The player who initiated the robbing.
     * @param robbed The player who is being robbed.
     */
    public StartRobbingEvent(Player robber, Player robbed) {
        this.robber = robber;
        this.robbed = robbed;
    }

    /**
     * Gets the player who initiated the robbing.
     *
     * @return The player who initiated the robbing.
     */
    public Player getRobber() {
        return this.robber;
    }

    /**
     * Gets the player who is being robbed.
     *
     * @return The player who is being robbed.
     */
    public Player getRobbed() {
        return this.robbed;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list for this event.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
