package com.frahhs.robbing.feature.kidnapping.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ToggleKidnapEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    protected final Player kidnapper;
    protected final Player kidnapped;
    protected final boolean isKidnapped;

    /**
     * Constructs a new ToggleHandcuffsEvent.
     *
     * @param kidnapper The player who is handcuffed.
     * @param kidnapped The player who handcuffed.
     * @param isKidnapped Whether the handcuffs are being put on or taken off.
     */
    public ToggleKidnapEvent(@NotNull final Player kidnapper, @NotNull final Player kidnapped, final boolean isKidnapped) {
        this.kidnapper = kidnapper;
        this.kidnapped = kidnapped;
        this.isKidnapped = isKidnapped;
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
    public Player getKidnapper() {
        return this.kidnapper;
    }

    /**
     * Retrieves the player who handcuffed.
     *
     * @return The player who handcuffed.
     */
    public Player getKidnapped() {
        return this.kidnapped;
    }

    /**
     * Checks if the handcuffs are being put on.
     *
     * @return True if the handcuffs are being put on, false if they are being taken off.
     */
    public boolean isKidnapped() {
        return this.isKidnapped;
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
