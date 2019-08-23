package com.magmaguy.elitemobs;

/*
 * Created by MagmaGuy on 07/10/2016.
 */

import com.magmaguy.elitemobs.combatsystem.EliteMobDamagedByPlayerHandler;
import com.magmaguy.elitemobs.commands.CommandHandler;
import com.magmaguy.elitemobs.config.*;
import com.magmaguy.elitemobs.config.commands.CommandsConfig;
import com.magmaguy.elitemobs.config.custombosses.CustomBossesConfig;
import com.magmaguy.elitemobs.config.customloot.CustomLootConfig;
import com.magmaguy.elitemobs.config.enchantments.EnchantmentsConfig;
import com.magmaguy.elitemobs.config.events.EventsConfig;
import com.magmaguy.elitemobs.config.menus.MenusConfig;
import com.magmaguy.elitemobs.config.mobproperties.MobPropertiesConfig;
import com.magmaguy.elitemobs.config.npcs.NPCsConfig;
import com.magmaguy.elitemobs.config.potioneffects.PotionEffectsConfig;
import com.magmaguy.elitemobs.config.powers.PowersConfig;
import com.magmaguy.elitemobs.economy.VaultCompatibility;
import com.magmaguy.elitemobs.events.EventLauncher;
import com.magmaguy.elitemobs.gamemodes.nightmaremodeworld.DaylightWatchdog;
import com.magmaguy.elitemobs.gamemodes.zoneworld.Grid;
import com.magmaguy.elitemobs.items.customenchantments.CustomEnchantment;
import com.magmaguy.elitemobs.items.customenchantments.SoulbindEnchantment;
import com.magmaguy.elitemobs.items.customitems.CustomItem;
import com.magmaguy.elitemobs.items.potioneffects.PotionEffectApplier;
import com.magmaguy.elitemobs.mobconstructor.mobdata.PluginMobProperties;
import com.magmaguy.elitemobs.mobscanner.SuperMobScanner;
import com.magmaguy.elitemobs.npcs.NPCInitializer;
import com.magmaguy.elitemobs.playerdata.PlayerData;
import com.magmaguy.elitemobs.powerstances.MajorPowerStanceMath;
import com.magmaguy.elitemobs.powerstances.MinorPowerStanceMath;
import com.magmaguy.elitemobs.quests.QuestRefresher;
import com.magmaguy.elitemobs.runnables.EggRunnable;
import com.magmaguy.elitemobs.utils.NonSolidBlockTypes;
import com.magmaguy.elitemobs.versionnotifier.VersionChecker;
import com.magmaguy.elitemobs.versionnotifier.VersionWarner;
import com.magmaguy.elitemobs.worldguard.WorldGuardCompatibility;
import com.magmaguy.elitemobs.worlds.CustomWorldLoading;
import org.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EliteMobs extends JavaPlugin {

    public static List<World> validWorldList = new ArrayList();
    public static boolean worldguardIsEnabled = false;
    public static List<World> zoneBasedSpawningWorlds = new ArrayList<>();
    public static List<World> nightmareWorlds = new ArrayList<>();

    @Override
    public void onEnable() {

        //Enable stats
        Metrics metrics = new Metrics(this);

        Bukkit.getLogger().info(" _____ _     _____ _____ ________  ______________  _____");
        Bukkit.getLogger().info("|  ___| |   |_   _|_   _|  ___|  \\/  |  _  | ___ \\/  ___|");
        Bukkit.getLogger().info("| |__ | |     | |   | | | |__ | .  . | | | | |_/ /\\ `--.");
        Bukkit.getLogger().info("|  __|| |     | |   | | |  __|| |\\/| | | | | ___ \\ `--. \\");
        Bukkit.getLogger().info("| |___| |_____| |_  | | | |___| |  | \\ \\_/ / |_/ //\\__/ /");
        Bukkit.getLogger().info("\\____/\\_____/\\___/  \\_/ \\____/\\_|  |_/\\___/\\____/ \\____/");
        Bukkit.getLogger().info("By MagmaGuy");

        /*
        New config loading
         */
        initializeConfigs();

        if (worldguardIsEnabled)
            Bukkit.getLogger().warning("[EliteMobs] WorldGuard compatibility is enabled!");
        else
            Bukkit.getLogger().warning("[EliteMobs] WorldGuard compatibility is not enabled!");

        //Enable Vault
        try {
            VaultCompatibility.vaultSetup();
        } catch (Exception e) {
            Bukkit.getLogger().warning("[EliteMobs] Something went wrong with the vault configuration - your Vault " +
                    "version is probably not compatible with this EliteMobs version. Please contact the dev about this error.");
            VaultCompatibility.VAULT_ENABLED = false;
        }

        //Hook up all listeners, some depend on config
        EventsRegistrer.registerEvents();

        //Launch the local data cache
        PlayerData.initializePlayerData();
        PlayerData.synchronizeDatabases();

        //Get world list
        worldScanner();

        //Start the repeating tasks such as scanners
        launchRunnables();

        //Commands
        this.getCommand("elitemobs").setExecutor(new CommandHandler());

        //launch events
        EventLauncher eventLauncher = new EventLauncher();
        eventLauncher.eventRepeatingTask();

        //launch internal clock for attack cooldown
        EliteMobDamagedByPlayerHandler.launchInternalClock();

        /*
        Initialize mob values
         */
        PluginMobProperties.initializePluginMobValues();

        /*
        Cache animation vectors
         */
        MinorPowerStanceMath.initializeVectorCache();
        MajorPowerStanceMath.initializeVectorCache();

        /*
        Scan for loaded SuperMobs
         */
        SuperMobScanner.scanSuperMobs();

        /*
        Initialize NPCs
         */
        new NPCInitializer();

        /*
        Make sure entities are getting culled - necessary due to some plugins on some servers
         */
        EntityTracker.entityValidator();

        /*
        Check for new plugin version
         */
        VersionChecker.updateComparer();
        if (!VersionChecker.pluginIsUpToDate)
            this.getServer().getPluginManager().registerEvents(new VersionWarner(), this);

        /*
        Initialize anticheat block values
         */
        NonSolidBlockTypes.initializeNonSolidBlocks();

        /*
        Launch quests
         */
        QuestRefresher.generateNewQuestMenus();

        /*
        Load plugin worlds
         */
        CustomWorldLoading.startupWorldInitialization();

    }

    @Override
    public void onLoad() {
        //WorldGuard hook
        //todo: fix
        try {
            worldguardIsEnabled = WorldGuardCompatibility.initialize();
        } catch (NoClassDefFoundError | IllegalStateException ex) {
            Bukkit.getLogger().warning("[EliteMobs] Error loading WorldGuard. EliteMob-specific flags will not work." +
                    " Except if you just reloaded the plugin, in which case they will totally work.");
            worldguardIsEnabled = false;
        }

    }

    @Override
    public void onDisable() {

        Bukkit.getServer().getScheduler().cancelTasks(MetadataHandler.PLUGIN);

        EntityTracker.shutdownPurger();

        validWorldList.clear();
        zoneBasedSpawningWorlds.clear();

        //save cached data
        Bukkit.getScheduler().cancelTask(PlayerData.databaseSyncTaskID);
        Bukkit.getLogger().info("[EliteMobs] Saving EliteMobs databases...");
        PlayerData.saveDatabases();
        Bukkit.getLogger().info("[EliteMobs] All saved! Good night.");
        PlayerData.clearPlayerData();

    }

    public static void initializeConfigs() {
        DefaultConfig.initializeConfig();
        ItemSettingsConfig.initializeConfig();
        ProceduralItemGenerationSettingsConfig.initializeConfig();
        PlayerCacheDataConfig.initializeConfig();
        PlayerMaxGuildRankConfig.initializeConfig();
        PotionEffectsConfig.initializeConfigs();
        ConfigValues.initializeConfigurations();
        ConfigValues.initializeCachedConfigurations();
        EconomySettingsConfig.initializeConfig();
        EnchantmentsConfig.initializeConfigs();
        AntiExploitConfig.initializeConfig();
        CombatTagConfig.initializeConfig();
        GuildRankData.initializeConfig();
        PlayerMoneyData.initializeConfig();
        CustomBossesConfig.initializeConfigs();
        AntiExploitConfig.initializeConfig();
        AdventurersGuildConfig.initializeConfig();
        ValidWorldsConfig.initializeConfig();
        NPCsConfig.initializeConfigs();
        MenusConfig.initializeConfigs();
        PowersConfig.initializeConfigs();
        MobPropertiesConfig.initializeConfigs();
        CustomLootConfig.initializeConfigs();
        CustomItem.initializeCustomItems();
        CustomEnchantment.initializeCustomEnchantments();
        MobCombatSettingsConfig.initializeConfig();
        CommandsConfig.initializeConfigs();
        EventsConfig.initializeConfigs();
    }

    public static void worldScanner() {
        for (World world : Bukkit.getWorlds())
            if (ValidWorldsConfig.fileConfiguration.getBoolean("Valid worlds." + world.getName())) {
                validWorldList.add(world);
                if (ValidWorldsConfig.fileConfiguration.getBoolean("Zone-based elitemob spawning worlds." + world.getName()))
                    zoneBasedSpawningWorlds.add(world);
                if (ValidWorldsConfig.fileConfiguration.getBoolean("Nightmare mode worlds." + world.getName())) {
                    nightmareWorlds.add(world);
                    DaylightWatchdog.preventDaylight(world);
                }
            }

    }

    /*
    Repeating tasks that run as long as the server is on
     */
    private void launchRunnables() {
        if (!zoneBasedSpawningWorlds.isEmpty())
            Grid.initializeGrid();
        int eggTimerInterval = 20 * 60 * 10 / DefaultConfig.superMobStackAmount;
        PotionEffectApplier.potionEffectApplier();
        if (MobPropertiesConfig.getMobProperties().get(EntityType.CHICKEN).isEnabled() && DefaultConfig.superMobStackAmount > 0) {
            new EggRunnable().runTaskTimer(this, eggTimerInterval, eggTimerInterval);
            if (EnchantmentsConfig.getEnchantment(SoulbindEnchantment.key + ".yml").isEnabled())
                SoulbindEnchantment.soulbindWatchdog();
        }
    }

}
