package com.frahhs.robbing.feature.lockpicking.mcp;

import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Controller;
import org.bukkit.entity.Player;

public class LockpickController extends Controller {
    public void openGUI(Player player, RobbingBlock safe) {
        logger.fine("%s is using a lockpick on the safe: %s", player.getName(), safe.getUniqueId().toString());
        player.openInventory( (new LockpickGUI(safe)).getInventory() );
    }
}
