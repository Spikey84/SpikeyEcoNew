package io.github.spikey84.spikeyeco2.targets.types;

import io.github.spikey84.spikeyeco2.EcoManager;
import io.github.spikey84.spikeyeco2.targets.BaseTarget;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MineTarget extends BaseTarget {
    protected Material material;
    protected EcoManager ecoManager;
    protected boolean bypass;

    protected static HashMap<UUID, List<Location>> pos = new HashMap<UUID, List<Location>>();

    public MineTarget(Material material, double reward, boolean bypass, EcoManager ecoManager) {
        this.material = material;
        this.reward = reward;
        this.ecoManager = ecoManager;
        this.bypass = bypass;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(material) || event.isCancelled()) return;

        Player player = event.getPlayer();

        if (!pos.containsKey(player.getUniqueId())) pos.put(player.getUniqueId(), new ArrayList<Location>());

        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) return;
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
