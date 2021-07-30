package io.github.spikey84.spikeyeco2.challanges.commands;

import io.github.spikey84.spikeyeco2.challanges.ChallangeManager;
import io.github.spikey84.spikeyeco2.challanges.ChallengeInventory;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultipliersInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ChallengeOpenInventoryCommand implements CommandExecutor {
    private Plugin plugin;

    public ChallengeOpenInventoryCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;

        player.openInventory(new ChallengeInventory(plugin, player).getInventory());

        return true;
    }
}
