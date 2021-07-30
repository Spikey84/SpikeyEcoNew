package io.github.spikey84.spikeyeco2.challanges.implementation;

import io.github.spikey84.spikeyeco2.challanges.ChallangeManager;

import io.github.spikey84.spikeyeco2.challanges.Challenge;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class MineOre extends Challenge {
    @EventHandler
    public void mineOre(BlockBreakEvent event) {
        if(!ChallangeManager.currentChallenge.getChallengeClass().getClass().equals(this.getClass())) return;
        if(event.getBlock().getType().equals(ChallangeManager.currentChallenge.getTargetMat()) && !event.getPlayer().getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) addPoint(event.getPlayer()); else {
            if (ChallangeManager.currentChallenge.getOtherNames() == null) return;
            for (String s : ChallangeManager.currentChallenge.getOtherNames()) {
                if(event.getBlock().getType().equals(Material.valueOf(s.toUpperCase())) && !event.getPlayer().getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) addPoint(event.getPlayer());
            }
        }
    }

    @Override
    public String getAction() {
        return "Mine";
    }
}
