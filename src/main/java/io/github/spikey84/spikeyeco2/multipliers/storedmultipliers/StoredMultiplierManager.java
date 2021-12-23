package io.github.spikey84.spikeyeco2.multipliers.storedmultipliers;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.DatabaseManager;
import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.MultipliersDAO;
import io.github.spikey84.spikeyeco2.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class StoredMultiplierManager {
    private Plugin plugin;
    private MultiplierManager multiplierManager;

    private List<Multiplier> storedMultipliers;

    public StoredMultiplierManager(Plugin plugin, MultiplierManager multiplierManager) {
        this.multiplierManager = multiplierManager;
        storedMultipliers = Lists.newArrayList();
        this.plugin = plugin;
        storedMultipliers = Lists.newArrayList();

        SchedulerUtils.runDatabase((connection -> {
            storedMultipliers = StoredMultiplierDAO.getStoredMultipliers(connection);
        }));

    }

    public void addMultiplier(Multiplier multiplier) {
        storedMultipliers.add(multiplier);

        SchedulerUtils.runDatabaseAsync((connection -> {
            StoredMultiplierDAO.addStoredMultiplier(connection, multiplier);
        }));
    }

    public List<Multiplier> getMultipliersByPlayer(UUID uuid) {
        List<Multiplier> tmpMults = Lists.newArrayList();
        for (Multiplier multiplier : storedMultipliers) {
            if (multiplier.getUuid().equals(uuid)) tmpMults.add(multiplier);
        }
        return tmpMults;
    }

    public void activateMultiplier(Multiplier multiplier) {
        if (multiplierManager.hasMultiplier(multiplier.getUuid())) return;

        SchedulerUtils.runDatabaseAsync(connection -> {
            StoredMultiplierDAO.removeMultiplier(connection, multiplier.getID());
            Bukkit.getLogger().info("fdgdfsgsdfgdsfgdsfgsdffgdsdgsffgfdsgfgfsd");
        });

        storedMultipliers.remove(multiplier);
        multiplierManager.addMultiplier(multiplier);
    }

    public boolean hasMultiplier(UUID uuid) {
        return multiplierManager.hasMultiplier(uuid);
    }
}
