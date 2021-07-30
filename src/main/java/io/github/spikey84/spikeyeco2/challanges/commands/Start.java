package io.github.spikey84.spikeyeco2.challanges.commands;

import io.github.spikey84.spikeyeco2.challanges.ChallangeManager;
import io.github.spikey84.spikeyeco2.challanges.ChallengeType;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements CommandExecutor {
    private ChallangeManager challangeManager;

    public Start(ChallangeManager challangeManager) {
        this.challangeManager = challangeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        ChatUtils.alert((Player) sender, "Invalid Challenge.");
        return true;
    }
}
