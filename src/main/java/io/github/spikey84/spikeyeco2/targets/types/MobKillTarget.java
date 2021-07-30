package io.github.spikey84.spikeyeco2.targets.types;

import io.github.spikey84.spikeyeco2.EcoManager;
import io.github.spikey84.spikeyeco2.targets.BaseTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

public class MobKillTarget extends BaseTarget {
    private EntityType entityType;
    private EcoManager ecoManager;
    private boolean bypass;

    private static HashMap<UUID, List<EntityType>> kills = new HashMap<UUID, List<EntityType>>();

    public MobKillTarget(EntityType entityType, double reward, boolean bypass, EcoManager ecoManager) {
        this.entityType = entityType;
        this.reward = reward;
        this.ecoManager = ecoManager;
        this.bypass = bypass;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null || !event.getEntity().getType().equals(entityType) || event.isCancelled()) return;

        Player player = event.getEntity().getKiller();

        if (!kills.containsKey(player.getUniqueId())) kills.put(player.getUniqueId(), new ArrayList<EntityType>());

        if (kills.get(player.getUniqueId()).contains(event.getEntityType()) && !bypass) {
            if (kills.get(player.getUniqueId()).size() >= 2) kills.get(player.getUniqueId()).remove(0);
            kills.get(player.getUniqueId()).add(event.getEntityType());
            return;
        }

        ecoManager.addReward(event.getEntity().getKiller(), reward);

        Bukkit.getLogger().info(kills.get(player.getUniqueId()).toString());

        if (kills.get(player.getUniqueId()).size() >= 2) kills.get(player.getUniqueId()).remove(0);
        kills.get(player.getUniqueId()).add(event.getEntityType());
    }
}
