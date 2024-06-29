package com.frahhs.robbing.feature.lockpicking.mcp;

import com.frahhs.lightlib.block.LightBlock;
import com.frahhs.lightlib.feature.LightController;
import org.bukkit.entity.Player;

public class LockpickController extends LightController {
    public void openGUI(Player player, LightBlock safe) {
        logger.fine("%s is using a lockpick on the safe: %s", player.getName(), safe.getUniqueId().toString());
        player.openInventory( (new LockpickGUI(safe)).getInventory() );
    }
}
