package com.frahhs.robbing;

import co.aikar.commands.PaperCommandManager;
import com.frahhs.robbing.commands.RobbingCommand;
import com.frahhs.robbing.database.RBDatabase;
import com.frahhs.robbing.features.generic.listeners.CustomRecipesListener;
import com.frahhs.robbing.features.handcuffing.controllers.HandcuffsBarController;
import com.frahhs.robbing.features.handcuffing.listeners.HandcuffedListener;
import com.frahhs.robbing.features.handcuffing.listeners.HandcuffingListener;
import com.frahhs.robbing.features.handcuffing.listeners.HitHandcuffsListener;
import com.frahhs.robbing.features.kidnapping.listeners.KidnappingListener;
import com.frahhs.robbing.features.rob.listeners.CatchListener;
import com.frahhs.robbing.features.rob.listeners.RobListener;
import com.frahhs.robbing.items.ItemsManager;
import com.frahhs.robbing.items.rbitems.Handcuffs;
import com.frahhs.robbing.items.rbitems.Lockpick;
import com.frahhs.robbing.providers.ConfigProvider;
import com.frahhs.robbing.providers.MessagesProvider;
import com.frahhs.robbing.utils.RBLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Robbing extends JavaPlugin {
    private static Robbing instance;

    // Managers
    private ConfigProvider configProvider;
    private MessagesProvider messagesProvider;
    PaperCommandManager commandManager;

    // Database
    private RBDatabase rbDatabase;

    // Utils
    private RBLogger rbLogger;

    // Items
    private ItemsManager itemsManager;

    @Override
    public void onEnable() {
        instance = this;

        // Setup utils
        rbLogger = new RBLogger(this);
        rbLogger.setLevel(Level.ALL);

        // Setup managers
        configProvider = new ConfigProvider(this);
        messagesProvider = new MessagesProvider(this);
        itemsManager = new ItemsManager(this);
        commandManager  = new PaperCommandManager(this);

        // Setup Database connection
        rbDatabase = new RBDatabase(this);

        // Register stuff
        registerCommands();
        registerEvents();
        registerItems();

        // Disable plugin if is disabled in the config
        if(!configProvider.getBoolean("general.enabled"))
            this.getPluginLoader().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        rbDatabase.disable();

        // Remove all handcuffs bar
        HandcuffsBarController handcuffsBarController = new HandcuffsBarController();
        handcuffsBarController.onDisable();
    }

    public void reload() {
        // Items
        itemsManager.dispose();
        registerItems();

        // Config and messages providers
        configProvider.reload();
        messagesProvider.reload();
    }

    private void registerEvents() {
        // Generic
        getServer().getPluginManager().registerEvents(new CustomRecipesListener(),this);

        // Steal
        getServer().getPluginManager().registerEvents(new RobListener(),this);
        getServer().getPluginManager().registerEvents(new CatchListener(),this);

        // Handcuffs
        getServer().getPluginManager().registerEvents(new HandcuffingListener(),this);
        getServer().getPluginManager().registerEvents(new HandcuffedListener(),this);
        getServer().getPluginManager().registerEvents(new KidnappingListener(),this);
        getServer().getPluginManager().registerEvents(new HitHandcuffsListener(),this);
    }

    private void registerCommands() {
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new RobbingCommand());
    }

    private void registerItems() {
        itemsManager.registerItem(new Handcuffs());
        itemsManager.registerItem(new Lockpick());
    }

    public static Robbing getInstance() {
        return instance;
    }

    public RBLogger getRBLogger() {
        return rbLogger;
    }

    public RBDatabase getRBDatabase() {
        return rbDatabase;
    }

    public ConfigProvider getConfigProvider() {
        return configProvider;
    }

    public MessagesProvider getMessagesProvider() {
        return messagesProvider;
    }

    public ItemsManager getItemsManager() {
        return itemsManager;
    }
}
