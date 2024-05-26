package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Model;
import com.frahhs.robbing.feature.safe.SafeInventory;
import com.frahhs.robbing.feature.safe.SafePin;
import com.frahhs.robbing.feature.safe.SafeUnlockGUI;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SafeModel extends Model {
    private final SafePin pin;
    private final SafeUnlockGUI safeUnlockGUI;
    private final SafeInventory safeInventory;

    private SafeModel(SafePin pin, SafeUnlockGUI safeUnlockGUI, SafeInventory safeInventory) {
        this.pin = pin;
        this.safeUnlockGUI = safeUnlockGUI;
        this.safeInventory = safeInventory;
    }

    public SafePin getPin() {
        return pin;
    }

    public SafeUnlockGUI getSafeUnlockGUI() {
        return safeUnlockGUI;
    }

    public SafeInventory getSafeInventory() {
        return safeInventory;
    }

    protected void saveInventory(Inventory inventory, String pin) {
        NamespacedKey inventoryKey = new NamespacedKey(plugin, "inventory");
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        PersistentDataContainer container = safeInventory.getSafe().getArmorStand().getPersistentDataContainer();

        container.set(inventoryKey, PersistentDataType.STRING, ItemUtil.toBase64(inventory.getContents()));
        container.set(pinKey, PersistentDataType.STRING, pin);
    }

    public static SafeModel getFromSafe(RobbingBlock safe) {
        SafeInventory inventory = new SafeInventory(safe);
        SafePin pin = new SafePin(safe);
        SafeUnlockGUI gui = new SafeUnlockGUI(pin.getDigits(), safe);

        return new SafeModel(pin, gui, inventory);
    }

    protected static boolean isLocked(RobbingBlock safe) {
        PersistentDataContainer container = safe.getArmorStand().getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        return container.has(pinKey, PersistentDataType.STRING);
    }
}
