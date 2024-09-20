package com.frahhs.robbing.feature.kidnapping.listener;

import com.frahhs.lightlib.LightListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.robbing.dependencies.DependenciesManager;
import com.frahhs.robbing.dependencies.Dependency;
import com.frahhs.robbing.dependencies.worldguard.WorldGuardFlag;
import com.frahhs.robbing.feature.handcuffing.item.HandcuffsKey;
import com.frahhs.robbing.feature.handcuffing.mcp.Handcuffing;
import com.frahhs.robbing.feature.handcuffing.mcp.HandcuffingController;
import com.frahhs.robbing.feature.kidnapping.mcp.Kidnapping;
import com.frahhs.robbing.feature.kidnapping.mcp.KidnappingController;
import com.frahhs.robbing.feature.kidnapping.mcp.LocationPath;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class KidnappingListener extends LightListener {
    private final LocationPath locationPath;
    private final KidnappingController kidnappingController;
    private final HandcuffingController handcuffingController;

    public KidnappingListener() {
        locationPath = new LocationPath();
        kidnappingController = new KidnappingController();
        handcuffingController = new HandcuffingController();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        // Check if following is enabled
        if(!config.getBoolean("handcuffing.kidnap.enabled"))
            return;

        // Check if kidnapper is using main hand
        if(!e.getHand().equals(EquipmentSlot.HAND))
            return;

        // Check if target is a Player
        if(!(e.getRightClicked() instanceof Player))
            return;

        // check if target is handcuffed
        if(!Handcuffing.isHandcuffed((Player)e.getRightClicked()))
            return;

        // Check if kidnapper is using handcuffs key
        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        if(LightPlugin.getItemsManager().get(itemInMainHand) instanceof HandcuffsKey)
            return;

        // Check if kidnapper have kidnap permissions
        if(!e.getPlayer().hasPermission("robbing.kidnap")) {
            String message = messages.getMessage("general.no_permissions");
            e.getPlayer().sendMessage(message);
            return;
        }

        Player kidnapper = e.getPlayer();
        Player kidnapped = (Player) e.getRightClicked();

        // Check if the player is just handcuffed, if true set to false
        // Without this check, the player will be handcuffed and kidnapped at same time
        if(Handcuffing.isJustHandcuff(kidnapped)) {
            Handcuffing.setJustHandcuff(kidnapped, false);
            return;
        }

        // Check if kidnapped is already following someone
        if(Kidnapping.isKidnapped(kidnapped) && kidnappingController.getKidnapper(kidnapped) != kidnapper) {
            String message = messages.getMessage("follow.already_following_someone");
            kidnapper.sendMessage(message);
            return;
        }

        // Check if worldguard flag is deny
        if(DependenciesManager.haveDependency(Dependency.WORLDGUARD)) {
            if (WorldGuardFlag.checkKidnapFlag(kidnapper)) {
                String message = messages.getMessage("general.deny_region");
                kidnapper.sendMessage(message);
                return;
            }
        }

        // Add (or remove) kidnapped and kidnapper to follow List
        if(!Kidnapping.isKidnapper(kidnapper)) {
            kidnappingController.kidnap(kidnapper, kidnapped);
            String message = messages.getMessage("follow.make_follow_cuffed");
            message = message.replace("{target}", kidnapped.getDisplayName());
            kidnapper.sendMessage(message);
        } else {
            kidnappingController.free(kidnapped);
            String message = messages.getMessage("follow.make_unfollow_cuffed");
            message = message.replace("{target}", kidnapped.getDisplayName());
            kidnapper.sendMessage(message);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player kidnapper = e.getPlayer();

        // Check if kidnapper is moving
        if(e.getTo() == null)
            return;

        boolean sameLocation = e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ();
        if(!sameLocation) {
            if(Kidnapping.isKidnapper(kidnapper)) {
                kidnappingController.getKidnapped(kidnapper);
                Player kidnapped = kidnappingController.getKidnapped(kidnapper);
                assert kidnapped != null;

                // Update pathing List
                locationPath.addLocationToPath(kidnapper, kidnapped);

                // Remove kidnapped condition
                boolean removeFollower = false;

                // Check if kidnapper is flying
                if(kidnapper.isFlying())
                    removeFollower = true;

                // Check if kidnapper is using elytra
                if(kidnapper.isGliding())
                    removeFollower = true;

                // Check remove kidnap condition
                if(removeFollower) {
                    kidnappingController.free(kidnapped);
                    String message = messages.getMessage("follow.make_unfollow_cuffed");
                    message = message.replace("{target}", kidnapped.getDisplayName());
                    kidnapper.sendMessage(message);
                    return;
                }

                // Make kidnapped follow
                locationPath.update(kidnapper, kidnapped);
            }
        }
    }

    /**
     * Free kidnapped after kidnapper quit
    */
    @EventHandler
    public void onKidnapperDisconnect(PlayerQuitEvent e) {
        // Check if following is enabled
        if(!config.getBoolean("handcuffing.kidnap.enabled"))
            return;

        if(!Kidnapping.isKidnapper(e.getPlayer()))
            return;

        KidnappingController controller = new KidnappingController();
        Kidnapping kidnapping = Kidnapping.getFromKidnapper(e.getPlayer());

        controller.free(kidnapping.getKidnapped());
    }

    /**
     * Free kidnapped after kidnapped quit
     */
    @EventHandler
    public void onKidnappedDisconnect(PlayerQuitEvent e) {
        // Check if following is enabled
        if(!config.getBoolean("handcuffing.kidnap.enabled"))
            return;

        if(!Kidnapping.isKidnapped(e.getPlayer()))
            return;

        KidnappingController controller = new KidnappingController();
        Kidnapping kidnapping = Kidnapping.getFromKidnapper(e.getPlayer());

        String message = messages.getMessage("follow.make_unfollow_cuffed");
        message = message.replace("{target}", e.getPlayer().getDisplayName());
        kidnapping.getKidnapper().sendMessage(message);

        controller.free(e.getPlayer());
    }
}
