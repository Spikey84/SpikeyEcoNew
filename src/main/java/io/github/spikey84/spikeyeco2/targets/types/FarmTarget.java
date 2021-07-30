package io.github.spikey84.spikeyeco2.targets.types;

import io.github.spikey84.spikeyeco2.EcoManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class FarmTarget extends MineTarget{

    public FarmTarget(Material material, double reward, boolean bypass, EcoManager ecoManager) {
        super(material, reward, bypass, ecoManager);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(material) || event.isCancelled()) return;

        Player player = event.getPlayer();

        if (!pos.containsKey(player.getUniqueId())) pos.put(player.getUniqueId(), new ArrayList<Location>());


        if(!event.getBlock().getType().equals(material) || event.isCancelled()) return;


        if (event.getBlock().getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) event.getBlock().getBlockData();
            if (ageable.getAge() != ageable.getMaximumAge()) return;
        }

        if (pos.get(player.getUniqueId()).contains(event.getBlock().getLocation()) && !bypass) {
            if (pos.get(player.getUniqueId()).size() >= 2) pos.get(player.getUniqueId()).remove(0);
            pos.get(player.getUniqueId()).add(event.getBlock().getLocation());
            return;
        }

        ecoManager.addReward(event.getPlayer(), reward);

        if (pos.get(player.getUniqueId()).size() >= 2) pos.get(player.getUniqueId()).remove(0);
        pos.get(player.getUniqueId()).add(event.getBlock().getLocation());
    }
}
