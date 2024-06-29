package com.frahhs.robbing;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.robbing.adapter.AdapterManager;
import com.frahhs.robbing.command.RobbingCommand;
import com.frahhs.robbing.dependencies.DependenciesManager;
import com.frahhs.robbing.feature.handcuffing.HandcuffingFeature;
import com.frahhs.robbing.feature.kidnapping.KidnappingFeature;
import com.frahhs.robbing.feature.lockpicking.LockpickingFeature;
import com.frahhs.robbing.feature.rob.RobbingFeature;
import com.frahhs.robbing.feature.safe.SafeFeature;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public final class Robbing extends LightPlugin {
    @Override
    public void onLightLoad() {
        LightPlugin.getOptions().setPermissionPrefix("robbing");
        LightPlugin.getOptions().setUpdateCheck(true);
        LightPlugin.getOptions().setSpigotMarketID("117484");
        LightPlugin.getOptions().setGithubContentsUrl("https://api.github.com/repos/FrahHS/Robbing/contents/robbing/src/main/resources/lang");
        LightPlugin.getOptions().setGithubUrlTemplate("https://raw.githubusercontent.com/FrahHS/Robbing/main/robbing/src/main/resources/lang/");

        DependenciesManager dependenciesManager = new DependenciesManager();
        dependenciesManager.init();
    }

    @Override
    public void onLightEnabled() {
        registerFeatures();
        registerCommands();

        // Do adaptions
        AdapterManager.adapt();
    }

    @Override
    public void onLightDisabled() {

    }

    public void registerFeatures() {
        LightPlugin.getFeatureManager().registerFeatures(new RobbingFeature(), this);
        LightPlugin.getFeatureManager().registerFeatures(new HandcuffingFeature(), this);
        LightPlugin.getFeatureManager().registerFeatures(new KidnappingFeature(), this);
        LightPlugin.getFeatureManager().registerFeatures(new SafeFeature(), this);
        LightPlugin.getFeatureManager().registerFeatures(new LockpickingFeature(), this);
        //featureManager.registerFeatures(new AtmFeature(), this);
    }

    private void registerCommands() {
        // Command settings
        getCommandManager().enableUnstableAPI("help");

        // Commands
        getCommandManager().registerCommand(new RobbingCommand(this));

        // Command completions
        getCommandManager().getCommandCompletions().registerCompletion("RobbingItems", c -> {
            List<String> rbItems = new ArrayList<>();
            getItemsManager().getRegisteredItems().forEach((item) -> {
                if(item.isGivable()) {
                    rbItems.add(item.getIdentifier());
                }
            });
            return ImmutableList.copyOf(rbItems);
        });
    }
}
