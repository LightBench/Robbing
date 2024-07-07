package com.frahhs.robbing.feature.lockpicking.event;

import com.frahhs.lightlib.block.LightBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered when after that a player used a lockpick.
 */
public class LockpickEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected final Player robber;
    protected final LightBlock safe;
    protected final boolean cracked;

    /**
     * Constructs a new ToggleHandcuffsEvent.
     *
     * @param robber The player who is handcuffed.
     * @param safe The player who handcuffed.
     * @param cracked Whether the handcuffs are being put on or taken off.
     */
    public LockpickEvent(@NotNull final Player robber, @NotNull final LightBlock safe, final boolean cracked) {
        this.robber = robber;
        this.safe = safe;
        this.cracked = cracked;
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
     * Retrieves the player who is lockpicking.
     *
     * @return The player involved.
     */
    public Player getRobber() {
        return this.robber;
    }

    /**
     * Retrieves the safe involved.
     *
     * @return The safe involved.
     */
    public LightBlock getSafe() {
        return this.safe;
    }

    /**
     * Checks if the safe was cracked or not.
     *
     * @return True if the safe was cracked, false otherwise.
     */
    public boolean isCracked() {
        return this.cracked;
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
