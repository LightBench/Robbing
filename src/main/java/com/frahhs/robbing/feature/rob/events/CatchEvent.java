package com.frahhs.robbing.feature.rob.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event where a player catches another player.
 */
public class CatchEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected final Player catcher;
    protected final Player caught;
    protected final boolean isCaught;

    /**
     * Constructs a new CatchEvent.
     *
     * @param catcher The player who is catching.
     * @param caught The player who is being caught.
     * @param isCaught Whether the catch is successful or not.
     */
    public CatchEvent(@NotNull final Player catcher, @NotNull final Player caught, final boolean isCaught) {
        this.catcher = catcher;
        this.caught = caught;
        this.isCaught = isCaught;
        this.cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Retrieves the player who is catching.
     *
     * @return The player who is catching.
     */
    public Player getCatcher() {
        return this.catcher;
    }

    /**
     * Retrieves the player who is being caught.
     *
     * @return The player who is being caught.
     */
    public Player getCaught() {
        return this.caught;
    }

    /**
     * Checks if the catch is successful.
     *
     * @return True if the catch is successful, false otherwise.
     */
    public boolean isCaught() {
        return this.isCaught;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Retrieves the static list of handlers for the event.
     *
     * @return The static list of handlers.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
