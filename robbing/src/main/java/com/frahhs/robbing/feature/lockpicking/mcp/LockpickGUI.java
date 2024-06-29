package com.frahhs.robbing.feature.lockpicking.mcp;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.block.LightBlock;
import com.frahhs.lightlib.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LockpickGUI implements GUI {
    private final Inventory inventory;
    private final LightBlock safe;
    private final int correctCylinder;

    public static final int SLOT_CYLINDER_1 = 18;
    public static final int SLOT_CYLINDER_2 = 19;
    public static final int SLOT_CYLINDER_3 = 20;
    public static final int SLOT_CYLINDER_4 = 21;
    public static final int SLOT_CYLINDER_5 = 22;
    public static final int SLOT_CYLINDER_6 = 23;
    public static final int SLOT_CYLINDER_7 = 24;
    public static final int SLOT_CYLINDER_8 = 25;
    public static final int SLOT_CYLINDER_9 = 26;

    public LockpickGUI(LightBlock safe) {
        this.inventory = Bukkit.createInventory(this, 6*9, "\uF001§f\uD83E\uDDE0");
        this.safe = safe;

        this.inventory.setItem(SLOT_CYLINDER_1, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_2, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_3, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_4, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_5, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_6, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_7, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_8, LightPlugin.getItemsManager().get("cylinder").getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_9, LightPlugin.getItemsManager().get("cylinder").getItemStack());

        // Generate random integers in range 1 to 26
        Random rand = new Random();
        this.correctCylinder = rand.nextInt(9) + 18;
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public int getCorrectCylinder() {
        return correctCylinder;
    }

    public LightBlock getSafe() {
        return safe;
    }

    public boolean isCylinderSlot(int slot) {
        switch(slot) {
            case SLOT_CYLINDER_1:
            case SLOT_CYLINDER_2:
            case SLOT_CYLINDER_3:
            case SLOT_CYLINDER_4:
            case SLOT_CYLINDER_5:
            case SLOT_CYLINDER_6:
            case SLOT_CYLINDER_7:
            case SLOT_CYLINDER_8:
            case SLOT_CYLINDER_9:
                return true;
        }
        return false;
    }
}
