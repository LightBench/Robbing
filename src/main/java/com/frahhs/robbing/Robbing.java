package com.frahhs.robbing;

import co.aikar.commands.PaperCommandManager;
import com.frahhs.robbing.commands.RobbingCommand;
import com.frahhs.robbing.database.RBDatabase;
import com.frahhs.robbing.block.listeners.RobbingBlockListener;
import com.frahhs.robbing.features.generics.listeners.CustomRecipesListener;
import com.frahhs.robbing.features.handcuffing.controllers.HandcuffsBarController;
import com.frahhs.robbing.features.handcuffing.listeners.HandcuffedListener;
import com.frahhs.robbing.features.handcuffing.listeners.HandcuffingListener;
import com.frahhs.robbing.features.handcuffing.listeners.HitHandcuffsListener;
import com.frahhs.robbing.features.kidnapping.listeners.KidnappingListener;
import com.frahhs.robbing.features.robbing.listeners.CatchListener;
import com.frahhs.robbing.features.robbing.listeners.RobListener;
import com.frahhs.robbing.item.ItemsManager;
import com.frahhs.robbing.item.items.Handcuffs;
import com.frahhs.robbing.item.items.Lockpick;
import com.frahhs.robbing.item.items.Safe;
import com.frahhs.robbing.providers.ConfigProvider;
import com.frahhs.robbing.providers.MessagesProvider;
import com.frahhs.robbing.utils.RobbingLogger;
import com.google.common.collect.ImmutableList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
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
    private RobbingLogger robbingLogger;

    // Items
    private ItemsManager itemsManager;

    @Override
    public void onEnable() {
        //TODO: alarm system
        instance = this;

        // Setup utils
        robbingLogger = new RobbingLogger(this);
        robbingLogger.setLevel(Level.ALL);

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
        // Config and messages providers
        configProvider.reload();
        messagesProvider.reload();

        // Items
        itemsManager.dispose();
        registerItems();
    }

    private void registerEvents() {
        // Generic
        getServer().getPluginManager().registerEvents(new CustomRecipesListener(),this);
        getServer().getPluginManager().registerEvents(new RobbingBlockListener(),this);

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

        commandManager.getCommandCompletions().registerCompletion("RBItems", c -> {
            List<String> rbItems = new ArrayList<>();
            itemsManager.getRegisteredItems().forEach(item -> {
                rbItems.add(item.getItemName());
            });
            return ImmutableList.copyOf(rbItems);
        });
    }

    private void registerItems() {
        itemsManager.registerItem(new Handcuffs());
        itemsManager.registerItem(new Lockpick());
        itemsManager.registerItem(new Safe());
    }

    public static Robbing getInstance() {
        return instance;
    }

    public RobbingLogger getRBLogger() {
        return robbingLogger;
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
