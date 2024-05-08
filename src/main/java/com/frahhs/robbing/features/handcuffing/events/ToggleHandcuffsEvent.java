package com.frahhs.robbing.features.handcuffing.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered when toggling handcuffs on a player.
 */
public class ToggleHandcuffsEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected final Player handcuffed;
    protected final Player handcuffer;
    protected final boolean isPuttingOn;

    /**
     * Constructs a new ToggleHandcuffsEvent.
     *
     * @param handcuffed The player who is handcuffed.
     * @param handcuffer The player who handcuffed.
     * @param isPuttingOn Whether the handcuffs are being put on or taken off.
     */
    public ToggleHandcuffsEvent(@NotNull final Player handcuffed, @NotNull final Player handcuffer, final boolean isPuttingOn) {
        this.handcuffed = handcuffed;
        this.handcuffer = handcuffer;
        this.isPuttingOn = isPuttingOn;
        cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Retrieves the player who is handcuffed.
     *
     * @return The handcuffed player.
     */
    public Player getHandcuffed() {
        return this.handcuffed;
    }

    /**
     * Retrieves the player who handcuffed.
     *
     * @return The player who handcuffed.
     */
    public Player getHandcuffer() {
        return this.handcuffer;
    }

    /**
     * Checks if the handcuffs are being put on.
     *
     * @return True if the handcuffs are being put on, false if they are being taken off.
     */
    public boolean isPuttingOn() {
        return this.isPuttingOn;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
