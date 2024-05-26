package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Controller;
import com.frahhs.robbing.feature.safe.SafeInventory;
import com.frahhs.robbing.feature.safe.bag.SafeInventoryBag;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SafeController extends Controller {
    public void openGUI(RobbingBlock safe, Player player) {
        SafeModel safeModel;
        if(SafeModel.isLocked(safe)) {
            safeModel = SafeModel.getFromSafe(safe);
        } else {
            safeModel = SafeModel.getFromSafe(safe);
            player.sendMessage(safeModel.getPin().toString());
        }

        player.openInventory(safeModel.getSafeUnlockGUI().getInventory());
    }

    public void openInventory(RobbingBlock safe, Player player) {
        SafeModel safeModel;
        if(SafeModel.isLocked(safe)) {
            safeModel = SafeModel.getFromSafe(safe);
        } else {
            safeModel = SafeModel.getFromSafe(safe);
            player.sendMessage(safeModel.getPin().toString());
        }

        // TODO: not to be here, but in a provider.
        SafeInventoryBag safeInventoryBag = (SafeInventoryBag) Robbing.getInstance().getBagManager().getBag("SafeInventoryBag");
        safeInventoryBag.getData().put(safe.getArmorStand().getUniqueId(), safeModel.getSafeInventory());

        player.openInventory(safeModel.getSafeInventory().getInventory());
    }

    public void update(RobbingBlock safe, Inventory inventory) {
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        safeModel.saveInventory(inventory, safeModel.getPin().toString());
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
        container.set(pinKey, PersistentDataType.STRING, safeModel.getPin().toString());

        if(SafeModel.isLocked(safe)) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "Locked");
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        player.getWorld().dropItemNaturally(safe.getLocation(), item);
    }
}
