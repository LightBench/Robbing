package com.frahhs.robbing;

import co.aikar.commands.PaperCommandManager;
import com.frahhs.robbing.block.RobbingBlockListener;
import com.frahhs.robbing.command.RobbingCommand;
import com.frahhs.robbing.database.RobbingDatabase;
import com.frahhs.robbing.dependencies.DependenciesManager;
import com.frahhs.robbing.feature.FeatureManager;
import com.frahhs.robbing.feature.handcuffing.HandcuffingFeature;
import com.frahhs.robbing.feature.kidnapping.KidnappingFeature;
import com.frahhs.robbing.feature.rob.RobbingFeature;
import com.frahhs.robbing.feature.safe.SafeFeature;
import com.frahhs.robbing.gui.GUIListener;
import com.frahhs.robbing.item.ItemManager;
import com.frahhs.robbing.item.items.*;
import com.frahhs.robbing.provider.ConfigProvider;
import com.frahhs.robbing.provider.MessagesProvider;
import com.frahhs.robbing.util.logging.FatalCode;
import com.frahhs.robbing.util.logging.RobbingLogger;
import com.frahhs.robbing.util.bag.BagManager;
import com.google.common.collect.ImmutableList;
import jdk.jfr.internal.LogLevel;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Robbing extends JavaPlugin {
    private static Robbing instance;
    private static RobbingLogger robbingLogger;

    // Providers
    private ConfigProvider configProvider;
    private MessagesProvider messagesProvider;

    // Managers
    private PaperCommandManager commandManager;
    private DependenciesManager dependenciesManager;
    private BagManager bagManager;
    private ItemManager itemManager;
    private FeatureManager featureManager;

    // Database
    private RobbingDatabase robbingDatabase;

    @Override
    public void onEnable() {
        instance = this;

        // Enable logger
        robbingLogger = new RobbingLogger(this);
        robbingLogger.setLevel(Level.FINEST);

        // Enable managers
        configProvider = new ConfigProvider(this);
        messagesProvider = new MessagesProvider(this);
        itemManager = new ItemManager(this);
        commandManager  = new PaperCommandManager(this);
        bagManager = new BagManager();
        featureManager = new FeatureManager(this);

        // Enable Database connection
        robbingDatabase = new RobbingDatabase(this);

        // Register stuff
        registerCommands();
        registerEvents();
        registerItems();
        registerFeatures();

        // Disable plugin if is disabled in the config
        if(!configProvider.getBoolean("enabled"))
            this.getPluginLoader().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        // Disable database
        robbingDatabase.disable();

        // Disable features
        featureManager.disableFeatures();

        // Disable bags
        bagManager.disableBags();

        // Close logger
        robbingLogger.close();
    }

    @Override
    public void onLoad() {
        // Load dependencies
        dependenciesManager = new DependenciesManager();
        dependenciesManager.init();
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
        getServer().getPluginManager().registerEvents(new GUIListener(),this);
    }

    private void registerCommands() {
        // Command settings
        commandManager.enableUnstableAPI("help");

        // Commands
        commandManager.registerCommand(new RobbingCommand(this));

        // Command completions
        commandManager.getCommandCompletions().registerCompletion("RobbingItems", c -> {
            List<String> rbItems = new ArrayList<>();
            itemManager.getRegisteredItems().forEach(item -> rbItems.add(item.getName()));
            return ImmutableList.copyOf(rbItems);
        });
    }

    public void registerFeatures() {
        featureManager.registerFeatures(new RobbingFeature(this));
        featureManager.registerFeatures(new HandcuffingFeature(this));
        featureManager.registerFeatures(new KidnappingFeature(this));
        featureManager.registerFeatures(new SafeFeature(this));
    }

    private void registerItems() {
        itemManager.registerItems(new Handcuffs(this));
        itemManager.registerItems(new HandcuffsKey(this));
        itemManager.registerItems(new Lockpick(this));
        itemManager.registerItems(new Safe(this));
        itemManager.registerItems(new PanelNumber0(this));
        itemManager.registerItems(new PanelNumber1(this));
        itemManager.registerItems(new PanelNumber2(this));
        itemManager.registerItems(new PanelNumber3(this));
        itemManager.registerItems(new PanelNumber4(this));
        itemManager.registerItems(new PanelNumber5(this));
        itemManager.registerItems(new PanelNumber6(this));
        itemManager.registerItems(new PanelNumber7(this));
        itemManager.registerItems(new PanelNumber8(this));
        itemManager.registerItems(new PanelNumber9(this));
        itemManager.registerItems(new PanelNumberCancel(this));
        itemManager.registerItems(new PanelNumberCheck(this));

    }

    public static RobbingLogger getRobbingLogger() {
        return robbingLogger;
    }

    public RobbingDatabase getRobbingDatabase() {
        return robbingDatabase;
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
