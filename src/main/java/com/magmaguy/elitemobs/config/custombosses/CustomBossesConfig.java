package com.magmaguy.elitemobs.config.custombosses;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.TheReturnedConfig;
import com.magmaguy.elitemobs.config.UnusedNodeHandler;
import com.magmaguy.elitemobs.config.custombosses.premade.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CustomBossesConfig {

    private static HashMap<String, FileConfiguration> customBossConfigList = new HashMap<>();

    public static HashMap<String, FileConfiguration> getCustomBossConfigList() {
        return customBossConfigList;
    }

    public static FileConfiguration getCustomBossConfig(String fileName) {
        return customBossConfigList.get(fileName);
    }

    private static ArrayList<CustomBossConfigFields> customBossConfigFieldsList = new ArrayList<>(Arrays.asList(
            new ZombieKingConfig(),
            new TreasureGoblinConfig(),
            new NecronomiconZombieConfig(),
            new NecronomiconSkeletonConfig(),
            new TheReturnedConfig(),
            new BalrogConfig(),
            new RaugConfig(),
            new ZombieFriendConfig(),
            new ZombieMomConfig(),
            new ZombieDadConfig(),
            new FireFaeConfig(),
            new IceFaeConfig(),
            new LightningFaeConfig()
    ));

    /**
     * Initializes all configurations and stores them in a static list for later access
     */
    public static void initializeConfigurations() {
        //Check if the directory doesn't exist
        if (!Files.isDirectory(Paths.get(MetadataHandler.PLUGIN.getDataFolder().getPath() + "/custombosses"))) {
            generateFreshConfigurations();
            return;
        }
        //Runs if the directory exists

        //Check if all the defaults exist
        for (File file : (new File(MetadataHandler.PLUGIN.getDataFolder().getPath() + "/custombosses")).listFiles()) {
            boolean isPremade = false;
            for (CustomBossConfigFields customBossConfigFields : customBossConfigFieldsList) {
                if (file.getName().equalsIgnoreCase(customBossConfigFields.getFileName())) {
                    customBossConfigFieldsList.remove(customBossConfigFields);
                    initializeConfiguration(customBossConfigFields);
                    isPremade = true;
                    break;
                }
            }
            if (!isPremade)
                initializeConfiguration(file);
        }

        if (!customBossConfigFieldsList.isEmpty())
            generateFreshConfigurations();

    }

    /**
     * Called when the appropriate configurations directory does not exist
     *
     * @return
     */
    private static void generateFreshConfigurations() {
        for (CustomBossConfigFields customBossConfigFields : customBossConfigFieldsList)
            customBossConfigList.put(customBossConfigFields.getFileName(), initializeConfiguration(customBossConfigFields));
    }

    /**
     * Initializes a single instance of a premade configuration using the default values.
     *
     * @param customBossConfigFields
     * @return
     */
    private static FileConfiguration initializeConfiguration(CustomBossConfigFields customBossConfigFields) {

        File file = new File(MetadataHandler.PLUGIN.getDataFolder().getPath() + "/custombosses", customBossConfigFields.getFileName());

        if (!file.exists())
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ex) {
                Bukkit.getLogger().warning("[EliteMobs] Error generating the plugin file: " + file.getName());
            }

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.addDefaults(customBossConfigFields.generateConfigDefaults(fileConfiguration));
        fileConfiguration.options().copyDefaults(true);
        UnusedNodeHandler.clearNodes(fileConfiguration);

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        customBossConfigList.put(file.getName(), fileConfiguration);

        return fileConfiguration;

    }

    /**
     * Called when a user-made mob is detected.
     *
     * @return
     */
    private static FileConfiguration initializeConfiguration(File file) {
        //TODO: add actual checks of what people are putting in here
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        customBossConfigList.put(file.getName(), fileConfiguration);
        return fileConfiguration;
    }

}