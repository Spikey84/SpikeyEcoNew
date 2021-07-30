package io.github.spikey84.spikeyeco2.multipliers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.spikey84.spikeyeco2.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MultiplierManager {
    private Plugin plugin;

    private List<Multiplier> activeMultipliers;

    public MultiplierManager(Plugin plugin) {
        activeMultipliers = Lists.newArrayList();
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try(Connection connection = DatabaseManager.getConnection()) {
                activeMultipliers = MultipliersDAO.getActiveMultipliers(connection);
            } catch (SQLException e) {
                e.printStackTrace();
                activeMultipliers = Lists.newArrayList();
            }
        });
    }

    public void addMultiplier(Multiplier multiplier) {
        activeMultipliers.removeIf(mult -> mult.getUuid().equals(multiplier.getUuid()));

        activeMultipliers.add(multiplier);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Connection connection = DatabaseManager.getConnection();
                MultipliersDAO.addMultiplier(connection, multiplier);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void checkMultiplier() {
        List<Multiplier> toRemove = null;
        for (Multiplier multiplier : activeMultipliers) {
            if (Bukkit.getPlayer(multiplier.getUuid()) == null) continue;

            if (multiplier.getTime() < 1) {

                if (toRemove == null) toRemove = Lists.newArrayList();
                toRemove.add(multiplier);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        Connection connection = DatabaseManager.getConnection();
                        MultipliersDAO.removeMultiplier(connection, multiplier.getUuid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            multiplier.setTime(multiplier.getTime()-1);
        }
        if (toRemove != null)  {
            activeMultipliers.removeAll(toRemove);
            toRemove.clear();
        }
    }

    public void updateMultipliers() {
        for (Multiplier multiplier : activeMultipliers) {
            MultipliersDAO.addMultiplier(DatabaseManager.getConnection(), multiplier);
        }
    }

    public Multiplier getMultiplier(UUID uuid) {
        for (Multiplier multiplier : activeMultipliers) {
            if (multiplier.getUuid().equals(uuid)) return multiplier;
        }
        return null;
    }

    public boolean hasMultiplier(UUID uuid) {
        for (Multiplier multiplier : activeMultipliers) {
            if (multiplier.getUuid().equals(uuid)) return true;
        }
        return false;
    }
}
