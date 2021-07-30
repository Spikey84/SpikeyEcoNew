package io.github.spikey84.spikeyeco2.multipliers.commands;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultipliersInventory;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MultiplierOpenInventoryCommand implements CommandExecutor, TabCompleter {
    private StoredMultiplierManager storedMultiplierManager;
    private Plugin plugin;
    private MultiplierManager multiplierManager;

    public MultiplierOpenInventoryCommand(StoredMultiplierManager storedMultiplierManager, Plugin plugin, MultiplierManager multiplierManager) {
        this.storedMultiplierManager = storedMultiplierManager;
        this.plugin = plugin;
        this.multiplierManager = multiplierManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players.");
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equals("info")) {
            if (!multiplierManager.hasMultiplier(player.getUniqueId())) {
                ChatUtils.alert(player, "You have no active multiplier! Use " + ChatColor.DARK_AQUA + "/multiplier gui"  + ChatColor.WHITE +  " to activate one.");
                return true;
            }
            Multiplier multiplier = multiplierManager.getMultiplier(player.getUniqueId());
            ChatUtils.positiveChat(player, String.format("%sYou currently have a x%s multiplier for %s.", ChatColor.WHITE, ChatColor.GREEN +  "" + multiplier.getMultiplier() + "" + ChatColor.WHITE,ChatColor.DARK_AQUA + "" + DurationFormatUtils.formatDuration(multiplier.getTime()*1000,"H:mm:ss", false) + "" + ChatColor.WHITE));
            return true;
        }

        player.openInventory(new StoredMultipliersInventory(player, storedMultiplierManager, plugin, multiplierManager).getInventory());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> strs = Lists.newArrayList();
        if(strings.length < 2) {
            strs.add("info");
            strs.add("gui");
        }
        return strs;
    }
}
