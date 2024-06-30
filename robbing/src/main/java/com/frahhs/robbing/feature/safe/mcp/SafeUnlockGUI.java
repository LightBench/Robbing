package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.block.LightBlock;
import com.frahhs.lightlib.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SafeUnlockGUI implements GUI {
    private final LightBlock safe;
    private final SafePin correctPin;
    private final Inventory inventory;

    private final List<Integer> insertDigits = new ArrayList<>();

    public static final int SLOT_PANEL_0 = 38;
    public static final int SLOT_PANEL_1 = 10;
    public static final int SLOT_PANEL_2 = 11;
    public static final int SLOT_PANEL_3 = 12;
    public static final int SLOT_PANEL_4 = 19;
    public static final int SLOT_PANEL_5 = 20;
    public static final int SLOT_PANEL_6 = 21;
    public static final int SLOT_PANEL_7 = 28;
    public static final int SLOT_PANEL_8 = 29;
    public static final int SLOT_PANEL_9 = 30;
    public static final int SLOT_PANEL_CHECK = 39;
    public static final int SLOT_PANEL_CANCEL = 37;

    public static final int SLOT_PIN_1 = 14;
    public static final int SLOT_PIN_2 = 15;
    public static final int SLOT_PIN_3 = 16;
    public static final int SLOT_PIN_4 = 17;

    public SafeUnlockGUI(SafePin pin, LightBlock safe) {
        if(pin == null)
            throw new RuntimeException("Pin is not nullable");

        this.correctPin = pin;
        this.safe = safe;
        this.inventory = Bukkit.createInventory(this, 6*9, "\uF001§f\uD83D\uDE97");

        this.inventory.setItem(SLOT_PANEL_0 ,getItemFromSlot(SLOT_PANEL_0));
        this.inventory.setItem(SLOT_PANEL_1 ,getItemFromSlot(SLOT_PANEL_1));
        this.inventory.setItem(SLOT_PANEL_2 ,getItemFromSlot(SLOT_PANEL_2));
        this.inventory.setItem(SLOT_PANEL_3 ,getItemFromSlot(SLOT_PANEL_3));
        this.inventory.setItem(SLOT_PANEL_4 ,getItemFromSlot(SLOT_PANEL_4));
        this.inventory.setItem(SLOT_PANEL_5 ,getItemFromSlot(SLOT_PANEL_5));
        this.inventory.setItem(SLOT_PANEL_6 ,getItemFromSlot(SLOT_PANEL_6));
        this.inventory.setItem(SLOT_PANEL_7 ,getItemFromSlot(SLOT_PANEL_7));
        this.inventory.setItem(SLOT_PANEL_8 ,getItemFromSlot(SLOT_PANEL_8));
        this.inventory.setItem(SLOT_PANEL_9 ,getItemFromSlot(SLOT_PANEL_9));

        this.inventory.setItem(SLOT_PANEL_CANCEL ,getItemFromSlot(SLOT_PANEL_CANCEL));
        this.inventory.setItem(SLOT_PANEL_CHECK ,getItemFromSlot(SLOT_PANEL_CHECK));
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public LightBlock getSafe() {
        return safe;
    }

    public boolean checkPin() {
        SafePin pin = new SafePin(insertDigits);
        return pin.equals(correctPin);
    }

    public void cancelPin() {
        insertDigits.clear();
        this.inventory.setItem(SLOT_PIN_1, new ItemStack(Material.AIR));
        this.inventory.setItem(SLOT_PIN_2, new ItemStack(Material.AIR));
        this.inventory.setItem(SLOT_PIN_3, new ItemStack(Material.AIR));
        this.inventory.setItem(SLOT_PIN_4, new ItemStack(Material.AIR));
    }

    public void insertPinDigit(int digitSlot) {
        switch (insertDigits.size()) {
            case 0:
                insertDigits.add(getValueFromPanelSlot(digitSlot));
                this.inventory.setItem(SLOT_PIN_1, getItemFromSlot(digitSlot));
                break;
            case 1:
                insertDigits.add(getValueFromPanelSlot(digitSlot));
                this.inventory.setItem(SLOT_PIN_2, getItemFromSlot(digitSlot));
                break;
            case 2:
                insertDigits.add(getValueFromPanelSlot(digitSlot));
                this.inventory.setItem(SLOT_PIN_3, getItemFromSlot(digitSlot));
                break;
            case 3:
                insertDigits.add(getValueFromPanelSlot(digitSlot));
                this.inventory.setItem(SLOT_PIN_4, getItemFromSlot(digitSlot));
                break;
        }
    }

    private ItemStack getItemFromSlot(int slot) {
        switch(slot) {
            case SLOT_PANEL_0:
                return LightPlugin.getItemsManager().get("panel_number_0").getItemStack();
            case SLOT_PANEL_1:
                return LightPlugin.getItemsManager().get("panel_number_1").getItemStack();
            case SLOT_PANEL_2:
                return LightPlugin.getItemsManager().get("panel_number_2").getItemStack();
            case SLOT_PANEL_3:
                return LightPlugin.getItemsManager().get("panel_number_3").getItemStack();
            case SLOT_PANEL_4:
                return LightPlugin.getItemsManager().get("panel_number_4").getItemStack();
            case SLOT_PANEL_5:
                return LightPlugin.getItemsManager().get("panel_number_5").getItemStack();
            case SLOT_PANEL_6:
                return LightPlugin.getItemsManager().get("panel_number_6").getItemStack();
            case SLOT_PANEL_7:
                return LightPlugin.getItemsManager().get("panel_number_7").getItemStack();
            case SLOT_PANEL_8:
                return LightPlugin.getItemsManager().get("panel_number_8").getItemStack();
            case SLOT_PANEL_9:
                return LightPlugin.getItemsManager().get("panel_number_9").getItemStack();
            case SLOT_PANEL_CANCEL:
                return LightPlugin.getItemsManager().get("panel_number_cancel").getItemStack();
            case SLOT_PANEL_CHECK:
                return LightPlugin.getItemsManager().get("panel_number_check").getItemStack();
        }

        return null;
    }

    private Integer getValueFromPanelSlot(int slot) {
        switch(slot) {
            case SLOT_PANEL_0:
                return 0;
            case SLOT_PANEL_1:
                return 1;
            case SLOT_PANEL_2:
                return 2;
            case SLOT_PANEL_3:
                return 3;
            case SLOT_PANEL_4:
                return 4;
            case SLOT_PANEL_5:
                return 5;
            case SLOT_PANEL_6:
                return 6;
            case SLOT_PANEL_7:
                return 7;
            case SLOT_PANEL_8:
                return 8;
            case SLOT_PANEL_9:
                return 9;
        }

        return null;
    }
}
