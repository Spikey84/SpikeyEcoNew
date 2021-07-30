package io.github.spikey84.spikeyeco2.challanges;

import io.github.spikey84.spikeyeco2.EcoManager;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Challenge implements Listener {
    public int setComplete(Player player) {
        if(ChallangeManager.challengeComplete.contains(player)) return -1;
        int place = ChallangeManager.challengeComplete.size();
        ChallangeManager.challengeComplete.add(player.getUniqueId());
        ChatUtils.challengeBroadcast("Challenge completed by " + "" + ChatColor.DARK_AQUA + "" + player.getName() + "" + ChatColor.WHITE + ". Paying out: " + ChatColor.GREEN + "" + ChallangeManager.currentPrice + "" + ChatColor.WHITE + "" + String.format("("+ ChatColor.DARK_AQUA + "%s" + ChatColor.WHITE + ")", ChatUtils.getSuffix(place)));
        ChallangeManager.ecoManager.addReward(player, ChallangeManager.currentPrice);
        if(place <= 3) ChallangeManager.currentPrice = (int) (ChallangeManager.currentPrice * 0.90); else ChallangeManager.currentPrice = (int) (ChallangeManager.currentPrice * 0.50);
        if(ChallangeManager.currentPrice < ChallangeManager.minPrice) ChallangeManager.currentPrice= ChallangeManager.minPrice;
        return place;
    }

    public void addPoint(Player player) {
        if(ChallangeManager.challengeComplete.contains(player.getUniqueId())) return;
        if(ChallangeManager.challengeProgress.containsKey(player.getUniqueId())) {
            ChallangeManager.challengeProgress.put(player.getUniqueId(), ChallangeManager.challengeProgress.get(player.getUniqueId())+1);
        } else {
            ChallangeManager.challengeProgress.put(player.getUniqueId(),1);
        }
        if(ChallangeManager.challengeProgress.get(player.getUniqueId()) >= ChallangeManager.requiredPoints) setComplete(player); else ChatUtils.challengeChat(player, String.format("Your challenge progress is now " +  ChatUtils.getColor(ChallangeManager.challengeProgress.get(player.getUniqueId()), ChallangeManager.requiredPoints, "g") + "%s" + ChatColor.WHITE + "/" + ChatColor.WHITE + "%s.", ChallangeManager.challengeProgress.get(player.getUniqueId()), ChallangeManager.requiredPoints));
    }

    public String getAction() {
        return "Do";
    }


}
