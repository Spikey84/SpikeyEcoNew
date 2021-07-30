package io.github.spikey84.spikeyeco2;

import io.github.spikey84.spikeyeco2.challanges.ChallangeManager;
import io.github.spikey84.spikeyeco2.challanges.commands.ChallengeOpenInventoryCommand;
import io.github.spikey84.spikeyeco2.challanges.commands.CheckChallengeCommand;
import io.github.spikey84.spikeyeco2.challanges.commands.Start;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.commands.MultiplierAddCommand;
import io.github.spikey84.spikeyeco2.multipliers.commands.MultiplierAddUUID;
import io.github.spikey84.spikeyeco2.multipliers.commands.MultiplierOpenInventoryCommand;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultiplierManager;
import io.github.spikey84.spikeyeco2.targets.TargetType;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class SpikeyEco extends JavaPlugin {
    private JavaPlugin plugin;
    private FileConfiguration config = getConfig();

    private EcoManager ecoManager;
    private MultiplierManager multiplierManager;
    private StoredMultiplierManager storedMultiplierManager;
    private ChallangeManager challangeManager;

    private Economy econ = null;


    public void onEnable() {
        this.saveDefaultConfig();

        this.plugin = this;

        try (Connection connection = DatabaseManager.getConnection()) {
            DatabaseManager.createMultiplierTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!setupEconomy()) {
            ChatUtils.alertConsole("Vault has not be loaded");
        }

        this.multiplierManager = new MultiplierManager(plugin);
        this.ecoManager = new EcoManager(multiplierManager, econ);
        this.storedMultiplierManager = new StoredMultiplierManager(plugin, multiplierManager);
        this.challangeManager = new ChallangeManager(config, plugin, ecoManager);

        for (String key : config.getConfigurationSection("targets").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("targets." + key);
            boolean bypass = false;
            if (section.contains("bypass")) bypass = section.getBoolean("bypass");
            Bukkit.getPluginManager().registerEvents(TargetType.createNew(TargetType.valueOf(section.getString("type").toUpperCase()), section.getName().toUpperCase() , Float.parseFloat(section.getString("reward")), bypass,  ecoManager), this);
            if ((section.contains("others"))) {
                for (String s : section.getStringList("others")) {
                    String tmpS = s.replace("*", section.getName().toUpperCase()).toUpperCase();
                    Bukkit.getPluginManager().registerEvents(TargetType.createNew(TargetType.valueOf(section.getString("type").toUpperCase()), tmpS , Double.parseDouble(section.getString("reward")), bypass,  ecoManager), this);
                }
            }

        }
        ChatUtils.positiveConsole("Targets loaded.");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            multiplierManager.checkMultiplier();
        }, 1,20);

        getCommand("multiplieradd").setExecutor(new MultiplierAddCommand(multiplierManager, storedMultiplierManager));
        getCommand("multiplier").setExecutor(new MultiplierOpenInventoryCommand(storedMultiplierManager, plugin, multiplierManager));
        getCommand("multiplier").setTabCompleter(new MultiplierOpenInventoryCommand(storedMultiplierManager, plugin, multiplierManager));
        getCommand("challenge").setExecutor(new ChallengeOpenInventoryCommand(plugin));
        getCommand("challengeinfo").setExecutor(new CheckChallengeCommand());
        getCommand("multiplieradduuid").setExecutor(new MultiplierAddUUID(multiplierManager, storedMultiplierManager));
    }

    public void onDisable() {
        multiplierManager.updateMultipliers();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;

    }
}
