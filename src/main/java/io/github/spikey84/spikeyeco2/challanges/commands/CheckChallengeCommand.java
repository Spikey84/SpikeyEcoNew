package io.github.spikey84.spikeyeco2.challanges.commands;

import io.github.spikey84.spikeyeco2.challanges.ChallangeManager;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CheckChallengeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;

        ChatUtils.challengeChat(player, ChatColor.WHITE + "Current Challenge: " + ChatColor.BLUE + "" + ChallangeManager.currentChallenge.getName());
                sender.sendMessage(String.format("To complete this challenge %s %s %s within %s for %s coins.", ChallangeManager.currentChallenge.getChallengeClass().getAction(), ChallangeManager.requiredPoints, ChallangeManager.currentChallenge.getName(), ChallangeManager.timeStr, ChallangeManager.currentPrice));
        return true;
    }
}
