package com.frahhs.robbing;

import co.aikar.commands.PaperCommandManager;
import com.frahhs.robbing.block.RobbingBlockListener;
import com.frahhs.robbing.command.RobbingCommand;
import com.frahhs.robbing.database.RBDatabase;
import com.frahhs.robbing.util.bag.BagManager;
import com.frahhs.robbing.feature.FeatureManager;
import com.frahhs.robbing.feature.handcuffing.HandcuffingFeature;
import com.frahhs.robbing.feature.kidnapping.KidnappingFeature;
import com.frahhs.robbing.feature.rob.RobbingFeature;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.items.Handcuffs;
import com.frahhs.robbing.item.items.Lockpick;
import com.frahhs.robbing.item.items.Safe;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.provider.MessagesProvider;
import com.frahhs.robbing.util.RobbingLogger;
import com.google.common.collect.ImmutableList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Robbing extends JavaPlugin {
    private static Robbing instance;

    // Providers
    private ConfigProvider configProvider;
    private MessagesProvider messagesProvider;

    // Managers
    private PaperCommandManager commandManager;
    private BagManager bagManager;
    private ItemManager itemManager;
    private FeatureManager featureManager;

    // Database
    private RBDatabase rbDatabase;

    // Utils
    private RobbingLogger robbingLogger;

    @Override
    public void onEnable() {
        // TODO: alarm system
        instance = this;

        // Setup utils
        robbingLogger = new RobbingLogger(this);
        robbingLogger.setLevel(Level.ALL);

        // Setup managers
        configProvider = new ConfigProvider(this);
        messagesProvider = new MessagesProvider(this);
        itemManager = new ItemManager(this);
        commandManager  = new PaperCommandManager(this);
        bagManager = new BagManager();
        featureManager = new FeatureManager(this);

        // Setup Database connection
        rbDatabase = new RBDatabase(this);

        // Register stuff
        registerCommands();
        registerEvents();
        registerItems();
        registerFeatures();

        // Disable plugin if is disabled in the config
        if(!configProvider.getBoolean("general.enabled"))
            this.getPluginLoader().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        // Disable database
        rbDatabase.disable();

        // Disable features
        featureManager.disableFeatures();

        // Disable bags
        bagManager.disableBags();
    }

    public static Robbing getInstance() {
        return instance;
    }

    public void reload() {
        // Config and messages providers
        configProvider.reload();
        messagesProvider.reload();

        // Item
        itemManager.dispose();
        registerItems();

        // Bag
        bagManager.disableBags();
        bagManager.enableBags();

        // Feature
        featureManager.disableFeatures();
        featureManager.enableFeatures();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new RobbingBlockListener(),this);
    }

    private void registerCommands() {
        // Command settings
        commandManager.enableUnstableAPI("help");

        // Commands
        commandManager.registerCommand(new RobbingCommand(this));

        // Command completions
        commandManager.getCommandCompletions().registerCompletion("RBItems", c -> {
            List<String> rbItems = new ArrayList<>();
            itemManager.getRegisteredItems().forEach(item -> rbItems.add(item.getItemName()));
            return ImmutableList.copyOf(rbItems);
        });
    }

    public void registerFeatures() {
        featureManager.registerFeatures(new RobbingFeature(this));
        featureManager.registerFeatures(new HandcuffingFeature(this));
        featureManager.registerFeatures(new KidnappingFeature(this));
    }

    private void registerItems() {
        itemManager.registerItem(new Handcuffs(this));
        itemManager.registerItem(new Lockpick(this));
        itemManager.registerItem(new Safe(this));
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

    public BagManager getBagManager() {
        return bagManager;
    }

    public ItemManager getItemsManager() {
        return itemManager;
    }
}
