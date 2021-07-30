package io.github.spikey84.spikeyeco2.multipliers.storedmultipliers;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.DatabaseManager;
import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.MultipliersDAO;
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

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
            try(Connection connection = DatabaseManager.getConnection()) {
                storedMultipliers = StoredMultiplierDAO.getStoredMultipliers(connection);
            } catch (SQLException e) {
                e.printStackTrace();
                storedMultipliers = Lists.newArrayList();
            }
        });
    }

    public void addMultiplier(Multiplier multiplier) {
        storedMultipliers.add(multiplier);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Connection connection = DatabaseManager.getConnection();
                StoredMultiplierDAO.addStoredMultiplier(connection, multiplier);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Connection connection = DatabaseManager.getConnection();
                StoredMultiplierDAO.removeMultiplier(connection, multiplier.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        storedMultipliers.remove(multiplier);
        multiplierManager.addMultiplier(multiplier);
    }

    public boolean hasMultiplier(UUID uuid) {
        return multiplierManager.hasMultiplier(uuid);
    }
}
