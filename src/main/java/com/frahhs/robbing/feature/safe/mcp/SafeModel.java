package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Model;
import com.frahhs.robbing.inventory.SafeInventory;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class SafeModel extends Model {
    private final String pin;
    private final SafeInventory safeInventory;

    private SafeModel(String pin, SafeInventory safeInventory) {
        this.pin = pin;
        this.safeInventory = safeInventory;
    }

    public String getPin() {
        return pin;
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
        PersistentDataContainer container = safe.getArmorStand().getPersistentDataContainer();

        NamespacedKey inventoryKey = new NamespacedKey(Robbing.getInstance(), "inventory");
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        SafeInventory inventory = new SafeInventory(safe);
        if(container.has(inventoryKey, PersistentDataType.STRING)) {
            ItemStack[] content = ItemUtil.fromBase64(container.get(inventoryKey, PersistentDataType.STRING));
            inventory.getInventory().setContents(content);
        }

        String pin;
        if(container.has(pinKey, PersistentDataType.STRING))
            pin = container.get(pinKey, PersistentDataType.STRING);
        else {
            Random random = new Random();
            pin = String.format("%04d", random.nextInt(10000));
            container.set(pinKey, PersistentDataType.STRING, pin);
        }

        return new SafeModel(pin, inventory);
    }

    protected static boolean isInit(RobbingBlock safe) {
        PersistentDataContainer container = safe.getArmorStand().getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        return container.has(pinKey, PersistentDataType.STRING);
    }
}
