package com.frahhs.robbing.feature.kidnapping.listeners;

import com.frahhs.robbing.BaseListener;
import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.feature.handcuffing.controller.HandcuffingController;
import com.frahhs.robbing.feature.handcuffing.model.Handcuffing;
import com.frahhs.robbing.feature.kidnapping.PathManager;
import com.frahhs.robbing.feature.kidnapping.controllers.KidnappingController;
import com.frahhs.robbing.feature.kidnapping.models.KidnappingModel;
import com.frahhs.robbing.item.RobbingMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

public class KidnappingListener extends BaseListener {
    private final PathManager pathManager;
    private final KidnappingController kidnappingController;
    private final HandcuffingController handcuffingController;

    public KidnappingListener() {
        pathManager = new PathManager();
        kidnappingController = new KidnappingController();
        handcuffingController = new HandcuffingController();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        // Check if following is enabled
        if(!config.getBoolean("handcuffing.following"))
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

        // Check if kidnapper is using cuffs
        if(e.getPlayer().getInventory().getItemInMainHand().equals(Robbing.getInstance().getItemsManager().get(RobbingMaterial.HANDCUFFS)))
            return;

        // Check if kidnapper have follow permissions
        if(!e.getPlayer().hasPermission("robbing.follow"))
            return;

        // Check if kidnapper is using handcuffs
        if(handcuffingController.handcuffsInHand(e.getPlayer()))
            return;

        Player kidnapper = e.getPlayer();
        Player kidnapped = (Player) e.getRightClicked();

        // Check if kidnapped is already following someone
        if(KidnappingModel.isKidnapped(kidnapped) && kidnappingController.getKidnapper(kidnapped) != kidnapper) {
            String message = messages.getMessage("follow.already_following_someone");
            kidnapper.sendMessage(message);
            return;
        }

        // Add (or remove) kidnapped and kidnapper to follow List
        if(!KidnappingModel.isKidnapper(kidnapper)){
            kidnappingController.kidnap(kidnapper, kidnapped);
            String message = messages.getMessage("follow.make_follow_cuffed");
            message = message.replace("{target}", kidnapped.getDisplayName());
            kidnapper.sendMessage(message);
        }
        else{
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
            if(KidnappingModel.isKidnapper(kidnapper)) {
                kidnappingController.getKidnapped(kidnapper);
                Player kidnapped = kidnappingController.getKidnapped(kidnapper);
                assert kidnapped != null;

                // Update pathing List
                pathManager.addLocationToPath(kidnapper, kidnapped);

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
                pathManager.update(kidnapper, kidnapped);
            }
        }
    }
}
