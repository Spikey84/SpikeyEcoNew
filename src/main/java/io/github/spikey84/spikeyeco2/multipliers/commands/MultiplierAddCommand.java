package io.github.spikey84.spikeyeco2.multipliers.commands;

import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultiplierManager;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MultiplierAddCommand implements CommandExecutor {
    private MultiplierManager multiplierManager;
    private StoredMultiplierManager storedMultiplierManager;

    public MultiplierAddCommand(MultiplierManager multiplierManager, StoredMultiplierManager storedMultiplierManager) {

        this.multiplierManager = multiplierManager;
        this.storedMultiplierManager = storedMultiplierManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(sender instanceof  Player) {
            if (sender.hasPermission("spikeyeco.addmultiplier")) return true;
        }

        if (strings.length < 3) {
            ChatUtils.alert((Player) sender, "Please enter a players name, a multiplier, and a time in seconds.");
            return true;
        }

        if (Bukkit.getOfflinePlayerIfCached(strings[0]) == null) {
            ChatUtils.alert((Player) sender, "This player has not joined the server.");
            return true;
        }

        double mult = Double.parseDouble(strings[1]);
        long time = Long.parseLong(strings[2]);


        UUID uuid = Bukkit.getOfflinePlayerIfCached(strings[0]).getUniqueId();

        storedMultiplierManager.addMultiplier(new Multiplier(uuid,mult, time));
        ChatUtils.positiveChat((Player) sender, String.format("Multiplier has been added to %s's account.", Bukkit.getOfflinePlayerIfCached(strings[0]).getName()));
        return true;
    }
}
