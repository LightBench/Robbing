package com.frahhs.robbing.util.update;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.util.logging.ConsoleColor;
import com.frahhs.robbing.util.logging.RobbingLogger;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker implements Listener {

    private String url = "https://api.spigotmc.org/legacy/update.php?resource=";
    private String id = "117484";

    private static boolean isAvailable = false;

    public boolean isAvailable() {
        return UpdateChecker.isAvailable;
    }

    @EventHandler
    public void onAdminJoin(PlayerJoinEvent event) {
        ConfigProvider config = Robbing.getInstance().getConfigProvider();

        // Check if new version is out
        if(config.getBoolean("update-check")) {
            UpdateChecker updateChecker = new UpdateChecker();
            updateChecker.check();
        }

        if(UpdateChecker.isAvailable && config.getBoolean("update-check")) {
            if(event.getPlayer().hasPermission("robbing.admin")) {
                String prefix = Robbing.getInstance().getMessagesProvider().getPrefix();
                String message =
                                prefix + ChatColor.DARK_GREEN + "New version of Robbing is out!\n" +
                                prefix + ChatColor.DARK_GREEN + "New version is: " + ChatColor.GOLD + getNewVersion() + ChatColor.DARK_GREEN + ".\n" +
                                prefix + ChatColor.DARK_GREEN + "Your actual version is: " + ChatColor.RED + Robbing.getInstance().getDescription().getVersion() + ChatColor.DARK_GREEN + ".\n" +
                                prefix + ChatColor.DARK_GREEN + "Download at: \n" +
                                prefix + ChatColor.DARK_GREEN + "https://www.spigotmc.org/resources/robbing.117484/";

                event.getPlayer().sendMessage(message);
            }
        }
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        ConfigProvider config = Robbing.getInstance().getConfigProvider();

        // Check if new version is out
        if(config.getBoolean("update-check")) {
            UpdateChecker updateChecker = new UpdateChecker();
            updateChecker.check();
        }

        // Check if new version is out
        if(UpdateChecker.isAvailable && config.getBoolean("update-check")) {
            RobbingLogger logger = Robbing.getRobbingLogger();
            logger.warning("=====================================================");
            logger.warning("New version of Robbing is out!");
            logger.warning("New version is: " + ConsoleColor.DARK_GREEN + getNewVersion() + ConsoleColor.YELLOW + ".");
            logger.warning("Your actual version is: " + ConsoleColor.RED + Robbing.getInstance().getDescription().getVersion() + ConsoleColor.YELLOW + "." );
            logger.warning("Download at:");
            logger.warning("https://www.spigotmc.org/resources/robbing.117484/");
            logger.warning("=====================================================");
        }
    }

    public String getNewVersion() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url + id).openConnection();
            connection.setRequestMethod("GET");
            String raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

            String remoteVersion;
            if(raw.contains("-")) {
                remoteVersion = raw.split("-")[0].trim();
            } else {
                remoteVersion = raw;
            }

            return remoteVersion;

        } catch (IOException e) {
            return "";
        }
    }

    public void check() {
        UpdateChecker.isAvailable = checkUpdate();
    }

    private boolean checkUpdate() {
        try {
            String localVersion = Robbing.getInstance().getDescription().getVersion();
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url + id).openConnection();
            connection.setRequestMethod("GET");
            String raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

            String remoteVersion;
            if(raw.contains("-")) {
                remoteVersion = raw.split("-")[0].trim();
            } else {
                remoteVersion = raw;
            }

            if(!localVersion.equalsIgnoreCase(remoteVersion))
                return true;

        } catch (IOException e) {
            return false;
        }
        return false;
    }

}