package com.frahhs.robbing.feature.kidnapping.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event where a player toggles the kidnapping state of another player.
 */
public class ToggleKidnapEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected final Player kidnapper;
    protected final Player kidnapped;
    protected final boolean isKidnapped;

    /**
     * Constructs a new ToggleKidnapEvent.
     *
     * @param kidnapper The player who is kidnapping.
     * @param kidnapped The player who is being kidnapped.
     * @param isKidnapped Whether the kidnapping is being initiated or terminated.
     */
    public ToggleKidnapEvent(@NotNull final Player kidnapper, @NotNull final Player kidnapped, final boolean isKidnapped) {
        this.kidnapper = kidnapper;
        this.kidnapped = kidnapped;
        this.isKidnapped = isKidnapped;
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
     * Retrieves the player who is kidnapping.
     *
     * @return The player who is kidnapping.
     */
    public Player getKidnapper() {
        return this.kidnapper;
    }

    /**
     * Retrieves the player who is being kidnapped.
     *
     * @return The player who is being kidnapped.
     */
    public Player getKidnapped() {
        return this.kidnapped;
    }

    /**
     * Checks if the kidnapping is being initiated.
     *
     * @return True if the kidnapping is being initiated, false if it is being terminated.
     */
    public boolean isKidnapped() {
        return this.isKidnapped;
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
