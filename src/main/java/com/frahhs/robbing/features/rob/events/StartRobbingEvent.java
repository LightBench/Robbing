package com.frahhs.robbing.features.rob.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event indicating the start of a robbing action between two players.
 */
public class StartRobbingEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected final Player robber;
    protected final Player robbed;

    /**
     * Constructs a new StartRobbingEvent.
     *
     * @param robber The player who initiated the robbing.
     * @param robbed The player who is being robbed.
     */
    public StartRobbingEvent(@NotNull final Player robber, @NotNull final Player robbed) {
        this.robber = robber;
        this.robbed = robbed;
        cancel = false;
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

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
