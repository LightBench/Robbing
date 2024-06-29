package com.frahhs.robbing.provider;

import com.frahhs.robbing.Robbing;
import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Manages localization for a Spigot plugin using YAML configuration files.
 */
public class MessagesProvider {
    private final JavaPlugin plugin;
    private String lang;
    private String prefix;
    private Map<String, FileConfiguration> languageConfigs;


    /**
     * Constructs a new MessagesManager.
     *
     * @param plugin The JavaPlugin instance owning this manager.
     */
    public MessagesProvider(JavaPlugin plugin) {
        this.plugin = plugin;
        this.lang = Robbing.getInstance().getConfigProvider().getString("language");
        this.prefix = Robbing.getInstance().getConfigProvider().getString("prefix");
        this.languageConfigs = new HashMap<>();

        loadLanguageFiles();
        if (!languageConfigs.containsKey(lang)) {
            Robbing.getRobbingLogger().warning("Language \"%s\" not found! English automatically selected.", lang);
            lang = "en";
        }
    }

    /**
     * Reloads the MessagesProvider, updating language settings and reloading language files.
     */
    public void reload() {
        this.lang = Robbing.getInstance().getConfigProvider().getString("language");
        this.prefix = Robbing.getInstance().getConfigProvider().getString("prefix");
        this.languageConfigs = new HashMap<>();

        loadLanguageFiles();
        if (!languageConfigs.containsKey(lang)) {
            Robbing.getRobbingLogger().warning("Language \"%s\" not found! English automatically selected.", lang);
            lang = "en";
        }
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
            Robbing.getRobbingLogger().warning("Language files '%s' not found.", lang);
            return null;
        }

        String message = "";

        if(!config.contains(key)) {
            FileConfiguration configEng = languageConfigs.get("en");
            if(!configEng.contains(key)) {
                Robbing.getRobbingLogger().error("The message path '%s' was not found, empty value used. Try to regen the lang folder.", key);
            } else {
                Robbing.getRobbingLogger().warning("The lang path '%s' does not exist for the language '%s', english used.", key, lang);
                message = configEng.getString(key);
            }
        } else {
            message = config.getString(key);
        }

        assert message != null;
        message = message.replace("&", "§");

        if(usePrefix)
            return String.format("%s%s", prefix, message);
        else
            return message;
    }

    /**
     * Retrieves a localized message for the given language and key.
     *
     * @param key The key of the message to retrieve.
     * @return The localized message, or null if not found.
     */
    public String getMessage(String key) {
        return getMessage(key, true);
    }

    /**
     * Retrieves the plugin prefix.
     *
     * @return The prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Loads language configuration files from the plugin's 'lang' folder.
     */
    private void loadLanguageFiles() {
        File messagesFolder = new File(plugin.getDataFolder(), "lang");
        if (!messagesFolder.exists()) {
            messagesFolder.mkdirs();
        }

        // Retrieve all file names inside the messages folder
        List<String> languagesYMLFileName = new ArrayList<>(getResources("lang"));

        // Save lang files
        for (String curYML : languagesYMLFileName) {
            File curYMLFile = new File(Robbing.getInstance().getDataFolder(), "lang/" + curYML);
            if (!curYMLFile.exists())
                Robbing.getInstance().saveResource("lang/" + curYML, false);
        }

        // Load YAML files from messages folder
        YamlUpdater.update();
        File[] languageFiles = messagesFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        assert languageFiles != null;
        for (File file : languageFiles) {
            String fileName = file.getName().replace(".yml", "");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.options().copyDefaults(true);
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

    private static class YamlUpdater {
        private static final String GITHUB_CONTENTS_URL = "https://api.github.com/repos/FrahHS/Robbing/contents/robbing/src/main/resources/lang";
        private static final String GITHUB_URL_TEMPLATE = "https://raw.githubusercontent.com/FrahHS/Robbing/main/robbing/src/main/resources/lang/";
        private static final String LOCAL_PATH_TEMPLATE = Robbing.getInstance().getDataFolder() + File.separator + "lang" + File.separator;

        public static void update() {
            try {
                List<String> languages = fetchAvailableLanguages();

                if (languages == null) {
                    throw new RuntimeException("Unable to retrieve languages files.");
                }

                for (String lang : languages) {
                    String githubUrl = GITHUB_URL_TEMPLATE + lang;
                    String localPath = LOCAL_PATH_TEMPLATE + lang;

                    Map<String, Object> githubYaml = downloadYaml(githubUrl);

                    File localFile = new File(localPath);
                    if (!localFile.exists()) {
                        saveYaml(localPath, githubYaml);
                    } else {
                        Map<String, Object> localYaml = loadYaml(localPath);
                        updateYaml(localYaml, githubYaml);
                        saveYaml(localPath, localYaml);
                    }
                }
            } catch (IOException | RuntimeException e) {
                Robbing.getRobbingLogger().warning("Error while trying to update language files, be sure to have the latest version of Robbing installed.");
            }
        }

        private static List<String> fetchAvailableLanguages() throws IOException {
            List<String> languages = new ArrayList<>();
            String response = httpGet(GITHUB_CONTENTS_URL);

            JsonArray jsonArray;
            if (response != null) {
                Gson gson = new Gson();
                try {
                    jsonArray = gson.fromJson(response, JsonArray.class);
                } catch (JsonSyntaxException e) {
                    return null;
                }

                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    String filename = jsonObject.get("name").getAsString();
                    if (filename.endsWith(".yml")) {
                        languages.add(filename);
                    }
                }
            }
            return languages;
        }

        private static String httpGet(String url) throws IOException {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        try (InputStream inputStream = entity.getContent();
                             InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                             BufferedReader reader = new BufferedReader(isr)) {
                            return reader.lines().collect(Collectors.joining("\n"));
                        }
                    }
                }
            }
            return null;
        }

        private static Map<String, Object> downloadYaml(String url) throws IOException {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        try (InputStream inputStream = entity.getContent();
                             InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                             BufferedReader reader = new BufferedReader(isr)) {
                            Yaml yaml = new Yaml();
                            return yaml.load(reader);
                        }
                    }
                }
            }
            return null;
        }

        private static Map<String, Object> loadYaml(String path) throws IOException {
            try (InputStream inputStream = Files.newInputStream(Paths.get(path));
                 InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {
                Yaml yaml = new Yaml();
                return yaml.load(reader);
            }
        }

        private static void saveYaml(String path, Map<String, Object> data) throws IOException {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
                yaml.dump(data, writer);
            }
        }

        private static void updateYaml(Map<String, Object> localYaml, Map<String, Object> githubYaml) {
            for (Map.Entry<String, Object> entry : githubYaml.entrySet()) {
                if (!localYaml.containsKey(entry.getKey())) {
                    localYaml.put(entry.getKey(), entry.getValue());
                } else if (entry.getValue() instanceof Map) {
                    updateYaml((Map<String, Object>) localYaml.get(entry.getKey()), (Map<String, Object>) entry.getValue());
                }
            }
        }
    }
}
