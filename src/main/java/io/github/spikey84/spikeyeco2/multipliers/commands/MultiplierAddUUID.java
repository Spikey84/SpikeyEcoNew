package io.github.spikey84.spikeyeco2.multipliers.commands;

import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultiplierManager;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import io.github.spikey84.spikeyeco2.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MultiplierAddUUID implements CommandExecutor {
    private MultiplierManager multiplierManager;
    private StoredMultiplierManager storedMultiplierManager;

    public MultiplierAddUUID(MultiplierManager multiplierManager, StoredMultiplierManager storedMultiplierManager) {

        this.multiplierManager = multiplierManager;
        this.storedMultiplierManager = storedMultiplierManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length < 3) {
            sender.sendMessage("Please enter a players name, a multiplier, and a time in seconds.");
            return true;
        }

        double mult = 1;
        long time = 1;

        try {
            mult = Double.parseDouble(strings[1]);
            time = Long.parseLong(strings[2]);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }


        UUID uuid = UUID.fromString(strings[0]);

        storedMultiplierManager.addMultiplier(new Multiplier(uuid,mult, time));
        //ChatUtils.positiveChat((Player) sender,"Multiplier has been added to account.");
        return true;
    }
}
