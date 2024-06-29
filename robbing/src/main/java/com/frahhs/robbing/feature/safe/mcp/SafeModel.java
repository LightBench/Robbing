package com.frahhs.robbing.feature.safe.mcp;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import com.frahhs.robbing.feature.Model;
import com.frahhs.robbing.feature.safe.bag.SafeInventoryBag;
import com.frahhs.robbing.util.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class SafeModel extends Model {
    RobbingBlock safe;
    SafeInventoryProvider safeInventoryProvider;
    SafePinProvider safePinProvider;

    private SafeModel(RobbingBlock safe) {
        this.safe = safe;
        this.safeInventoryProvider = new SafeInventoryProvider();
        this.safePinProvider = new SafePinProvider();
    }

    public SafeUnlockGUI getSafeUnlockGUI() {
        return new SafeUnlockGUI(getPin(), safe);
    }

    protected boolean haveInventory() {
        return safeInventoryProvider.getEntryByUUID(safe.getUniqueId().toString()) != null;
    }

    protected boolean havePin() {
        return safePinProvider.getSafePin(safe.getUniqueId()) != null;
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

        return new SafePin(safePinProvider.getSafePin(safe.getUniqueId()));
    }

    public void saveInventory(Inventory inventory) {
        if(safeInventoryProvider.getEntryByUUID(safe.getUniqueId().toString()) == null) {
            safeInventoryProvider.addEntry(safe.getUniqueId().toString(), ItemUtil.toBase64(inventory.getContents()));
        } else {
            safeInventoryProvider.updateEntry(safe.getUniqueId().toString(), ItemUtil.toBase64(inventory.getContents()));
        }
    }

    public void savePin(String pin, Player player) {
        if(safePinProvider.getSafePin(safe.getUniqueId()) == null) {
            safePinProvider.addLockedSafe(safe.getUniqueId(), player.getUniqueId(), pin);
        } else {
            safePinProvider.updateSafe(safe.getUniqueId(), pin);
        }
    }

    protected void removeInventory() {
        safeInventoryProvider.removeEntry(safe.getUniqueId().toString());

        SafeInventoryBag safeInventoryBag = (SafeInventoryBag) Robbing.getInstance().getBagManager().getBag("SafeInventoryBag");
        safeInventoryBag.getData().remove(safe.getUniqueId());
    }

    protected void removePin() {
        safePinProvider.deleteSafe(safe.getUniqueId());
    }

    public static boolean isLocked(RobbingBlock safe) {
        return getFromSafe(safe).havePin();
    }

    public static SafeModel getFromSafe(RobbingBlock safe) {
        return new SafeModel(safe);
    }

    public static List<SafeModel> getByPlayer(Player player) {
        SafePinProvider safePinProvider = new SafePinProvider();

        return safePinProvider.getSafesByPlayer(player.getUniqueId());
    }
}
