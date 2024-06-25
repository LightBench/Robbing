package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Controller;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

    public void open(RobbingBlock safe, Player player) {
        if(SafeModel.isLocked(safe))
            openGUI(safe, player);
        else
            openInventory(safe, player);
    }


    public void openGUI(RobbingBlock safe, Player player) {
        logger.fine("%s opened gui of the safe: %s", player.getName(), safe.getUniqueId().toString());
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        player.openInventory(safeModel.getSafeUnlockGUI().getInventory());
    }

    public void openInventory(RobbingBlock safe, Player player) {
        logger.fine("%s opened inventory of the safe: %s", player.getName(), safe.getUniqueId().toString());
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        player.openInventory(safeModel.getInventory());
    }

    public void update(RobbingBlock safe, Inventory inventory) {
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        safeModel.saveInventory(inventory);
    }

    public void placeBlock(RobbingBlock safe, ItemStack item) {
        logger.fine("Placing safe: %s", safe.getUniqueId().toString());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        SafeModel safeModel = SafeModel.getFromSafe(safe);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey inventoryKey = new NamespacedKey(Robbing.getInstance(), "inventory");
        NamespacedKey pinKey = new NamespacedKey(plugin, "pin");

        if(container.has(inventoryKey, PersistentDataType.STRING)) {
            SafeInventory safeInventory = new SafeInventory(safe);
            String base64Inventory = container.get(inventoryKey, PersistentDataType.STRING);
            safeInventory.getInventory().setContents(ItemUtil.fromBase64(base64Inventory));
            safeModel.saveInventory(safeInventory.getInventory());
        }

        if(container.has(pinKey, PersistentDataType.STRING)) {
            String pin = container.get(pinKey, PersistentDataType.STRING);
            safeModel.savePin(pin);
        }
    }

    public void dropBlock(RobbingBlock safe, Player player) {
        logger.fine("%s dropped the safe: %s", player.getName(), safe.getUniqueId().toString());
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        ItemStack item = plugin.getItemsManager().get(RobbingMaterial.SAFE).getItemStack();

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey inventoryKey = new NamespacedKey(Robbing.getInstance(), "inventory");
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        List<String> lore = new ArrayList<>();
        if(SafeModel.isLocked(safe)) {
            lore.add(ChatColor.GRAY + "Status: " + ChatColor.RED + "Locked");
            meta.setLore(lore);
        } else {
            safeModel.removeInventory();
            lore.add(ChatColor.GRAY + "Status: " + ChatColor.DARK_GREEN + "Free");
            meta.setLore(lore);
        }

        if(safeModel.haveInventory())
            container.set(inventoryKey, PersistentDataType.STRING, ItemUtil.toBase64(safeModel.getInventory().getContents()));
        if(safeModel.havePin())
            container.set(pinKey, PersistentDataType.STRING, safeModel.getPin().toString());


        item.setItemMeta(meta);
        player.getWorld().dropItemNaturally(safe.getLocation(), item);
    }

    public void dropInventory(RobbingBlock safe, Player player) {
        logger.fine("%s dropped the inventory of the safe: %s", player.getName(), safe.getUniqueId().toString());
        SafeModel safeModel = SafeModel.getFromSafe(safe);

        ItemStack[] content = safeModel.getInventory().getContents();

        for (ItemStack itemStack : content) {
            if(itemStack != null) {
                player.getWorld().dropItemNaturally(safe.getLocation(), itemStack);
                itemStack.setType(Material.AIR);
            }
        }

        safeModel.getInventory().setContents(content);
    }

    public void lock(RobbingBlock safe, String pin) {
        logger.fine("safe %s locked, pin: %s", safe.getUniqueId().toString(), pin);
        // If it is already locked, return
        if(SafeModel.isLocked(safe))
            return;

        SafeModel safeModel = SafeModel.getFromSafe(safe);
        safeModel.savePin(pin);
    }

    public void unlock(RobbingBlock safe) {
        logger.fine("safe %s unlocked", safe.getUniqueId().toString());
        // If it is already unlocked, return
        if(!SafeModel.isLocked(safe))
            return;

        SafeModel safeModel = SafeModel.getFromSafe(safe);
        safeModel.removePin();
    }
}
