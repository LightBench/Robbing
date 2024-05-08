package com.frahhs.robbing.features.block.events;

import com.frahhs.robbing.item.RobbingBlock;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class RobbingBlockEvent extends Event {
    protected RobbingBlock block;

    public RobbingBlockEvent(@NotNull final RobbingBlock theBlock) {
        block = theBlock;
    }

    /**
     * Gets the robbing block involved in this event.
     *
     * @return The Block which block is involved in this event
     */
    @NotNull
    public final RobbingBlock getBlock() {
        return block;
    }
}
