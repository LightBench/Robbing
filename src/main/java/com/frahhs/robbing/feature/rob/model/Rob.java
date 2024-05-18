package com.frahhs.robbing.feature.rob.model;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.Model;
import com.frahhs.robbing.feature.rob.provider.RobProvider;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.RobbingItem;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Model class representing a robbery event.
 */
public class Rob extends Model {
    private final Player robber;
    private final Player robbed;
    private final RobProvider provider;

    /**
     * Constructs a Rob object.
     *
     * @param robber The player who is the robber.
     * @param robbed The player who is robbed.
     */
    public Rob(Player robber, Player robbed) {
        this.robber = robber;
        this.robbed = robbed;
        this.provider = new RobProvider();
    }

    /**
     * Retrieves the robber player.
     *
     * @return The robber player.
     */
    public Player getRobber() {
        return robber;
    }

    /**
     * Retrieves the robbed player.
     *
     * @return The robbed player.
     */
    public Player getRobbed() {
        return robbed;
    }

    /**
     * Checks if a player is currently robbing.
     *
     * @param robber The player to check.
     * @return True if the player is currently robbing, otherwise false.
     */
    public static boolean isRobbingNow(Player robber) {
        RobProvider provider = new RobProvider();
        return provider.isRobbingNow(robber);
    }

    /**
     * Checks if a player is currently being robbed.
     *
     * @param robbed The player to check.
     * @return True if the player is currently being robbed, otherwise false.
     */
    public static boolean isRobbedNow(Player robbed) {
        RobProvider provider = new RobProvider();
        return provider.isRobbedNow(robbed);
    }

    /**
     * Sets the robbing action between a robber and a robbed player.
     *
     * @param robber The player initiating the robbery.
     * @param robbed The player being robbed.
     */
    public static void setRobbingNow(Player robber, Player robbed) {
        RobProvider provider = new RobProvider();
        provider.saveRobbingNow(robber, robbed);
    }

    /**
     * Removes the ongoing robbing action for a player.
     *
     * @param robber The player ending the robbery.
     */
    public static void removeRobbingNow(Player robber) {
        RobProvider provider = new RobProvider();
        provider.removeRobbingNow(robber);
    }

    /**
     * Checks if a player is currently under a robbery cooldown.
     *
     * @param handcuffer The player to check.
     * @return True if the player is under cooldown, otherwise false.
     */
    public static boolean haveCooldown(Player handcuffer) {
        RobProvider provider = new RobProvider();
        return provider.haveCooldown(handcuffer);
    }

    /**
     * Retrieves the cooldown for a player.
     *
     * @param handcuffer The player to check.
     * @return The timestamp when the robbing action occurred.
     */
    public static Cooldown getCooldown(Player handcuffer) {
        RobProvider provider = new RobProvider();
        return provider.getCooldown(handcuffer);
    }

    /**
     * Sets the cooldown for the robbing action.
     * <p>
     * Set to 0 to remove cooldown.
     *
     * @param handcuffer The player to set the cooldown for.
     * @param time       The duration of the cooldown in seconds.
     */
    public static void setCooldown(Player handcuffer, int time) {
        RobProvider provider = new RobProvider();

        new Thread(() -> {
            try {
                Cooldown cooldown = new Cooldown(System.currentTimeMillis(), time);
                provider.saveCooldown(handcuffer, cooldown);
                Thread.sleep(time * 1000L);
                provider.removeCooldown(handcuffer.getPlayer());
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    /**
     * Sets the default cooldown for the robbing action.
     *
     * @param handcuffer The player to set the default cooldown for.
     */
    public static void setCooldown(Player handcuffer) {
        ConfigProvider config = Robbing.getInstance().getConfigProvider();
        setCooldown(handcuffer, config.getInt("rob.cooldown"));
    }

    public static boolean itemIsRobbable(ItemStack item) {
        ConfigProvider config = Robbing.getInstance().getConfigProvider();

        ItemManager itemManager = Robbing.getInstance().getItemsManager();

        boolean whitelisted = false;
        boolean blacklisted = false;

        // Whitelist check
        if(config.getBoolean("rob.whitelist.enabled")) {
            for (String el : config.getStringList("rob.whitelist.items")) {
                // Robbing item check
                if(itemManager.isRegistered(item) ) {
                    if(el.startsWith("robbing:")) {
                        RobbingItem rbItem = itemManager.getByItemStack(item);
                        if(rbItem.getItemName().equalsIgnoreCase(el.replace("robbing:", ""))) {
                            whitelisted = true;
                        }

                    }
                }
                // Standard items
                else if(item.getType().equals(Material.getMaterial(el))) {
                    whitelisted = true;
                }
            }
        }

        // Blacklist check
        if(config.getBoolean("rob.blacklist.enabled")) {
            for (String el : config.getStringList("rob.blacklist.items")) {
                // Robbing item check
                if(itemManager.isRegistered(item) ) {
                    if(el.startsWith("robbing:")) {
                        RobbingItem rbItem = itemManager.getByItemStack(item);
                        if(rbItem.getItemName().equalsIgnoreCase(el.replace("robbing:", ""))) {
                            blacklisted = true;
                        }

                    }
                }
                // Standard items
                if(item.getType().equals(Material.matchMaterial(el))) {
                    blacklisted = true;
                }
            }
        }

        // Whitelist constraint
        if(config.getBoolean("rob.whitelist.enabled"))
            if(!whitelisted)
                return false;

        // Blacklist constraint
        if(config.getBoolean("rob.blacklist.enabled"))
            if(blacklisted)
                return false;

        return true;
    }
}
