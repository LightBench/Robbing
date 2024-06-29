package com.frahhs.robbing.adapter;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.RobbingListener;
import com.frahhs.robbing.block.events.RobbingBlockBreakEvent;
import com.frahhs.robbing.block.events.RobbingBlockInteractEvent;
import com.frahhs.robbing.feature.safe.mcp.SafeController;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SafePinAdapter extends RobbingListener {
    public static void adapt() {

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSafeOpen(RobbingBlockInteractEvent e) {
        if(!e.getBlock().getRobbingMaterial().equals(RobbingMaterial.SAFE))
            return;

        PersistentDataContainer container = e.getBlock().getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");
        if(!container.has(pinKey, PersistentDataType.STRING))
            return;

        SafeController controller = new SafeController();
        controller.lock(e.getBlock(), container.get(pinKey, PersistentDataType.STRING), e.getBlock().getPlacer());

        container.remove(pinKey);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSafeBreak(RobbingBlockBreakEvent e) {
        if(!e.getBlock().getRobbingMaterial().equals(RobbingMaterial.SAFE))
            return;

        PersistentDataContainer container = e.getBlock().getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");
        if(!container.has(pinKey, PersistentDataType.STRING))
            return;

        SafeController controller = new SafeController();
        controller.lock(e.getBlock(), container.get(pinKey, PersistentDataType.STRING), e.getBlock().getPlacer());

        container.remove(pinKey);
    }
}
