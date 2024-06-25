package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Model;
import com.frahhs.robbing.feature.safe.bag.SafeInventoryBag;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SafeModel extends Model {
    RobbingBlock safe;
    SafeInventoryProvider safeInventoryProvider;

    private SafeModel(RobbingBlock safe) {
        this.safe = safe;
        this.safeInventoryProvider = new SafeInventoryProvider();
    }

    public SafeUnlockGUI getSafeUnlockGUI() {
        return new SafeUnlockGUI(getPin(), safe);
    }

    protected boolean haveInventory() {
        return safeInventoryProvider.getEntryByUUID(safe.getUniqueId().toString()) != null;
    }

    protected boolean havePin() {
        PersistentDataContainer container = safe.getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");
        return container.has(pinKey, PersistentDataType.STRING);
    }

    public Inventory getInventory() {
        SafeInventoryBag safeInventoryBag = (SafeInventoryBag) Robbing.getInstance().getBagManager().getBag("SafeInventoryBag");
        SafeInventory safeInventory;

        if(safeInventoryBag.getData().containsKey(safe.getUniqueId())) {
            safeInventory = safeInventoryBag.getData().get(safe.getUniqueId());
        } else {
            safeInventory = new SafeInventory(safe);
            safeInventoryBag.getData().put(safe.getUniqueId(), safeInventory);

            // Handle Safe inventory
            SafeInventoryProvider.SafeInventoryEntry safeInventoryEntry = safeInventoryProvider.getEntryByUUID(safe.getUniqueId().toString());
            if(safeInventoryEntry != null) {
                ItemStack[] content = ItemUtil.fromBase64(safeInventoryEntry.getInventory());
                safeInventory.getInventory().setContents(content);
            }
        }

        return safeInventory.getInventory();
    }

    public SafePin getPin() {
        if(!havePin())
            return null;

        PersistentDataContainer container = safe.getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(plugin, "pin");
        String pin = container.get(pinKey, PersistentDataType.STRING);
        assert pin != null;

        return new SafePin(pin);
    }

    public void saveInventory(Inventory inventory) {
        if(safeInventoryProvider.getEntryByUUID(safe.getUniqueId().toString()) == null) {
            safeInventoryProvider.addEntry(safe.getUniqueId().toString(), ItemUtil.toBase64(inventory.getContents()));
        } else {
            safeInventoryProvider.updateEntry(safe.getUniqueId().toString(), ItemUtil.toBase64(inventory.getContents()));
        }
    }

    public void savePin(String pin) {
        PersistentDataContainer container = safe.getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");
        container.set(pinKey, PersistentDataType.STRING, pin);
    }

    protected void removeInventory() {
        safeInventoryProvider.removeEntry(safe.getUniqueId().toString());

        PersistentDataContainer container = safe.getPersistentDataContainer();
        NamespacedKey inventoryKey = new NamespacedKey(plugin, "inventory");
        container.remove(inventoryKey);

        SafeInventoryBag safeInventoryBag = (SafeInventoryBag) Robbing.getInstance().getBagManager().getBag("SafeInventoryBag");
        safeInventoryBag.getData().remove(safe.getUniqueId());
    }

    protected void removePin() {
        PersistentDataContainer container = safe.getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");
        container.remove(pinKey);
    }

    public static SafeModel getFromSafe(RobbingBlock safe) {
        return new SafeModel(safe);
    }

    public static boolean isLocked(RobbingBlock safe) {
        return getFromSafe(safe).havePin();
    }
}
