package com.frahhs.robbing.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ItemUtil {
    public static String toBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            BukkitObjectOutputStream data = new BukkitObjectOutputStream(str);
            data.writeInt(items.length);
            for(ItemStack item : items) {
                data.writeObject(item);
            }
            data.close();
            return Base64.getEncoder().encodeToString(str.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ItemStack[] fromBase64(String inventoryData) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(inventoryData));
            BukkitObjectInputStream data = new BukkitObjectInputStream(stream);
            ItemStack[] items = new ItemStack[data.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) data.readObject();
            }
            data.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
