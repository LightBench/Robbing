package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Controller;
import com.frahhs.robbing.inventory.SafeInventory;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SafeController extends Controller {
    public void open(RobbingBlock safe, Player player) {
        SafeModel safeModel;
        if(SafeModel.isInit(safe)) {
            safeModel = SafeModel.getFromSafe(safe);
        } else {
            safeModel = SafeModel.getFromSafe(safe);
            player.sendMessage(safeModel.getPin());
        }


        player.openInventory(safeModel.getSafeInventory().getInventory());
    }

    public void close(RobbingBlock safe, Inventory inventory) {
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        safeModel.saveInventory(inventory, safeModel.getPin());
    }

    public void placeBlock(RobbingBlock safe, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey inventoryKey = new NamespacedKey(Robbing.getInstance(), "inventory");
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        if(container.has(pinKey, PersistentDataType.STRING)) {
            SafeInventory inventory = new SafeInventory(safe);
            inventory.getInventory().setContents(ItemUtil.fromBase64(container.get(inventoryKey, PersistentDataType.STRING)));
            String pin = container.get(pinKey, PersistentDataType.STRING);
            SafeModel safeModel = SafeModel.getFromSafe(safe);
            safeModel.saveInventory(inventory.getInventory(), pin);
        }
    }

    public void dropBlock(RobbingBlock safe, Player player) {
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        ItemStack item = plugin.getItemsManager().get(RobbingMaterial.SAFE).getItemStack();

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey inventoryKey = new NamespacedKey(Robbing.getInstance(), "inventory");
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");
        container.set(inventoryKey, PersistentDataType.STRING, ItemUtil.toBase64(safeModel.getSafeInventory().getInventory().getContents()));
        container.set(pinKey, PersistentDataType.STRING, safeModel.getPin());

        item.setItemMeta(meta);

        player.getWorld().dropItemNaturally(safe.getLocation(), item);
    }
}
