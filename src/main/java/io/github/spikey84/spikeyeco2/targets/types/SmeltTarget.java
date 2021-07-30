package io.github.spikey84.spikeyeco2.targets.types;

import io.github.spikey84.spikeyeco2.EcoManager;
import io.github.spikey84.spikeyeco2.targets.BaseTarget;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public class SmeltTarget extends BaseTarget {
    private Material material;
    private EcoManager ecoManager;

    public SmeltTarget(Material material, double reward, EcoManager ecoManager) {
        this.material = material;
        this.reward = reward;
        this.ecoManager = ecoManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRemoveItem(FurnaceExtractEvent event) {
        if (!event.getItemType().equals(material) || event.getPlayer() == null) return;

        ecoManager.addReward(event.getPlayer(), reward * event.getItemAmount());

    }
}
