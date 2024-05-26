package com.frahhs.robbing.feature.safe;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.safe.bag.SafeInventoryBag;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class SafeInventory implements InventoryHolder {
    private final Inventory inventory;
    private final RobbingBlock safe;

    public SafeInventory(RobbingBlock safe) {
        this.safe = safe;

        // TODO: not to be here, but in a provider.
        SafeInventoryBag safeInventoryBag = (SafeInventoryBag) Robbing.getInstance().getBagManager().getBag("SafeInventoryBag");

        if(safeInventoryBag.getData().containsKey(safe.getArmorStand().getUniqueId())) {
            this.inventory = safeInventoryBag.getData().get(safe.getArmorStand().getUniqueId()).getInventory();
        }
        else {
            this.inventory = Bukkit.createInventory(this, 9 * 6, "Safe");

            PersistentDataContainer container = safe.getArmorStand().getPersistentDataContainer();
            NamespacedKey inventoryKey = new NamespacedKey(Robbing.getInstance(), "inventory");
            // Handle Safe inventory
            if(container.has(inventoryKey, PersistentDataType.STRING)) {
                ItemStack[] content = ItemUtil.fromBase64(container.get(inventoryKey, PersistentDataType.STRING));
                this.inventory.setContents(content);
            }
        }
    }

    public RobbingBlock getSafe() {
        return safe;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
