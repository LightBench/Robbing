package com.frahhs.robbing.managers;

import com.frahhs.robbing.Robbing;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Manages localization for a Spigot plugin using YAML configuration files.
 */
public class MessagesManager {
    private final JavaPlugin plugin;
    private String lang;
    private final String prefix;
    private final Map<String, FileConfiguration> languageConfigs;

    /**
     * Constructs a new MessagesManager.
     *
     * @param plugin The JavaPlugin instance owning this manager.
     */
    public MessagesManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.lang = Robbing.getInstance().getConfigManager().getString("general.language");
        this.prefix = Robbing.getInstance().getConfigManager().getString("general.prefix");
        this.languageConfigs = new HashMap<>();

        loadLanguageFiles();
        if (!languageConfigs.containsKey(lang)) {
            Robbing.getInstance().getRBLogger().warning(String.format("Language %s not found! English automatically selected.", lang));
            lang = "en";
        }
    }

    /**
     * Retrieves a localized message for the given language and key.
     *
     * @param key The key of the message to retrieve.
     * @return The localized message, or null if not found.
     */
    public String getMessage(String key) {
        FileConfiguration config = languageConfigs.get(lang);
        if (config == null) {
            Robbing.getInstance().getRBLogger().warning("Language files '%s' not found.", lang);
            return null;
        }

        if(!config.contains(key))
            Robbing.getInstance().getRBLogger().error("The message path '%s' does not exist.", key);

        String value = config.getString(key);
        return String.format("%s%s", prefix, value).replace("&", "§");
    }

    /**
     * Retrieves a localized message for the given language and key.
     *
     * @param key The key of the message to retrieve.
     * @param usePrefix If true the prefix will be added at the message head.
     * @return The localized message, or null if not found.
     */
    public String getMessage(String key, boolean usePrefix) {
        FileConfiguration config = languageConfigs.get(lang);
        if (config == null) {
            Robbing.getInstance().getRBLogger().warning("Language files '%s' not found.", lang);
            return null;
        }

        if(!config.contains(key))
            Robbing.getInstance().getRBLogger().error("The message path '%s' does not exist.", key);

        String value = config.getString(key);
        if(usePrefix)
            return String.format("%s%s", prefix, value);
        else
            return value;
    }

    /**
     * Loads language configuration files from the plugin's 'messages' folder.
     */
    private void loadLanguageFiles() {
        File messagesFolder = new File(plugin.getDataFolder(), "messages");
        if (!messagesFolder.exists()) {
            messagesFolder.mkdirs();
        }

        // Retrieve all file names inside the messages folder
        List<String> languagesYMLFileName = new ArrayList<>(getResources("messages"));

        // Save lang files
        for (String curYML : languagesYMLFileName) {
            File curYMLFile = new File(Robbing.getInstance().getDataFolder(), "messages/" + curYML);
            if (!curYMLFile.exists())
                Robbing.getInstance().saveResource("messages/" + curYML, false);
        }

        // Load YAML files from messages folder
        File[] languageFiles = messagesFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        assert languageFiles != null;
        for (File file : languageFiles) {
            // Update messages file if outdated: https://github.com/tchristofferson/Config-Updater
            /*try {
                ConfigUpdater.update(this.plugin, String.format("messages/%s", file.getName()), file, Collections.emptyList());
            } catch (IOException e) {
                Robbing.getInstance().getRBLogger().error("Unexpected error, send the following stacktrace to our staff: https://discord.gg/Hh9zMQnWvW");
                Robbing.getInstance().getRBLogger().error(e.toString());
            }*/

            String fileName = file.getName().replace(".yml", "");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            languageConfigs.put(fileName, config);
        }
    }

    /**
     * Retrieves resources (files) within a specified path.
     *
     * @param path The path to the resources.
     * @return A list of resource filenames inside the specified path.
     */
    private List<String> getResources(String path) {
        // Gets the resource path for the JAR file
        URL dirURL = plugin.getClass().getClassLoader().getResource(path);

        // Create the return list of resource filenames inside the provided path
        ArrayList<String> result = new ArrayList<String>();

        // Get the path of the JAR file
        assert dirURL != null;
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file

        // Decode the compiled JAR for iteration
        JarFile jar = null;
        try {
            jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Robbing.getInstance().getServer().getConsoleSender().sendMessage("ERROR - getResources() - couldn't decode the Jar file to index resources.");
        } catch (IOException ex) {
            Robbing.getInstance().getServer().getConsoleSender().sendMessage("ERROR - getResources() - couldn't perform IO operations on jar file");
        }

        // Gets all the elements in a JAR file for iterating through
        assert jar != null;
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar

        // Iterate through and add elements inside the "path" folder to the resources to be moved
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            // Check that the element starts with the path
            if (name.startsWith(path)) {
                String entry = name.substring(path.length() + 1);
                String last = name.substring(name.length() - 1);

                // Discard if it is a directory
                if (!last.equals(File.separator)) {
                    // Resource contains at least one character or number
                    if (entry.matches(".*[a-zA-Z0-9].*")) {
                        result.add(entry);
                    }
                }
            }
        }

        // Return the array of strings of filenames inside the path
        return Arrays.asList(result.toArray(new String[0]));
    }
}
