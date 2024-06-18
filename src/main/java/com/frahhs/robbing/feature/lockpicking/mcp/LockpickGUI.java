package com.frahhs.robbing.feature.lockpicking.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.gui.GUI;
import com.frahhs.robbing.gui.GUIType;
import com.frahhs.robbing.item.RobbingMaterial;
import com.frahhs.robbing.item.items.Cylinder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LockpickGUI implements GUI {
    private final Inventory inventory;
    private final RobbingBlock safe;
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

    public LockpickGUI(RobbingBlock safe) {
        this.inventory = Bukkit.createInventory(this, 6*9, "\uF001§f\uD83E\uDDE0");
        this.safe = safe;

        this.inventory.setItem(SLOT_CYLINDER_1, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_2, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_3, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_4, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_5, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_6, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_7, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_8, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());
        this.inventory.setItem(SLOT_CYLINDER_9, Robbing.getInstance().getItemsManager().get(RobbingMaterial.CYLINDER).getItemStack());

        // Generate random integers in range 1 to 26
        Random rand = new Random();
        this.correctCylinder = rand.nextInt(9) + 18;
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public GUIType getType() {
        return GUIType.LOCKPICK;
    }

    public int getCorrectCylinder() {
        return correctCylinder;
    }

    public RobbingBlock getSafe() {
        return safe;
    }
}
