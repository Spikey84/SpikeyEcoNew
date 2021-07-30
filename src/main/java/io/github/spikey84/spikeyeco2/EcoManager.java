package io.github.spikey84.spikeyeco2;

import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EcoManager {
    private MultiplierManager multiplierManager;
    private Economy economy;

    public EcoManager(MultiplierManager multiplierManager, Economy economy) {
        this.multiplierManager = multiplierManager;
        this.economy = economy;
    }

    public void addReward(Player player, double reward) {
        double multiplier = 1;

        if (multiplierManager.hasMultiplier(player.getUniqueId())) multiplier = multiplierManager.getMultiplier(player.getUniqueId()).getMultiplier();

        String s = String.format("%s has earned %s (x%s)", player.getName(), String.valueOf(reward*multiplier), multiplier);
        economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), (reward*multiplier));
        //ChatUtils.positiveChat(player, s);

    }
}
