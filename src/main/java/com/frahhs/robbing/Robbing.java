package com.frahhs.robbing;

import com.frahhs.robbing.commands.TestCommand;
import com.frahhs.robbing.database.RBDatabase;
import com.frahhs.robbing.features.rob.listeners.CatchRobberListener;
import com.frahhs.robbing.features.rob.listeners.RobListener;
import com.frahhs.robbing.items.RBItemStack;
import com.frahhs.robbing.managers.ConfigManager;
import com.frahhs.robbing.managers.MessagesManager;
import com.frahhs.robbing.utils.RBLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Robbing extends JavaPlugin {
    private static Robbing instance;

    // Managers
    private ConfigManager configManager;
    private MessagesManager messagesManager;

    // Database
    private RBDatabase rbDatabase;

    // Utils
    private RBLogger rbLogger;

    // Items
    private RBItemStack rbItemStack;

    @Override
    public void onEnable() {
        instance = this;

        // Setup utils
        rbLogger = new RBLogger(this);

        // Setup managers
        configManager   = new ConfigManager(this);
        messagesManager = new MessagesManager(this);

        // Setup Database connection
        rbDatabase = new RBDatabase(this);

        // Setup Robbing items
        rbItemStack = new RBItemStack(this);

        getCommand("robbing").setExecutor(new TestCommand());

        // Setup listeners
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {
        // Disable
        rbItemStack.disable();

        // Enable
        configManager   = new ConfigManager(this);
        messagesManager = new MessagesManager(this);
        rbItemStack     = new RBItemStack(this);
    }

    private void registerEvents() {
        // Generic
        //getServer().getPluginManager().registerEvents(new UpdateListener(),this);
        //getServer().getPluginManager().registerEvents(new CustomRecipesListener(),this);

        // Steal
        getServer().getPluginManager().registerEvents(new RobListener(),this);
        getServer().getPluginManager().registerEvents(new CatchRobberListener(),this);

        // Safes
        //getServer().getPluginManager().registerEvents(new SafeCrack(),this);
        //getServer().getPluginManager().registerEvents(new SafeUnbreakable(),this);
        //getServer().getPluginManager().registerEvents(new LockpickingListener(),this);
        //getServer().getPluginManager().registerEvents(new SafeMenuListener(), this);

        // Handcuffs
        //getServer().getPluginManager().registerEvents(new HandcuffingListener(),this);
        //getServer().getPluginManager().registerEvents(new FollowListener(),this);
        //getServer().getPluginManager().registerEvents(new BreakingHandcuffsListener(),this);
        //getServer().getPluginManager().registerEvents(new HandcuffCancelEventsListener(),this);
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

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public RBItemStack getRBItemStack() {
        return rbItemStack;
    }
}
