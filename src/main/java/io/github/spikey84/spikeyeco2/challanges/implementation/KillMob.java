package io.github.spikey84.spikeyeco2.challanges.implementation;

import io.github.spikey84.spikeyeco2.challanges.ChallangeManager;
import io.github.spikey84.spikeyeco2.challanges.Challenge;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMob extends Challenge {
    @EventHandler
    public void mobKill(EntityDeathEvent event) {
        if(!(ChallangeManager.currentChallenge.getChallengeClass().getClass() == this.getClass())) return;
        if(!(event.getEntity() instanceof Monster) || event.getEntity().getKiller() == null) return;
        if(event.getEntity().getType().equals(ChallangeManager.currentChallenge.getTargetEntity())) addPoint(event.getEntity().getKiller());
    }

    @Override
    public String getAction() {
        return "Kill";
    }
}
