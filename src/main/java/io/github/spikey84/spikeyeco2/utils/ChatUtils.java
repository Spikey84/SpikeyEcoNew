package io.github.spikey84.spikeyeco2.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;


public class ChatUtils {

    private static String prefix = ChatColor.WHITE + "[" + ChatColor.AQUA + "SpikeyEco" + ChatColor.WHITE + "]";
    private static String challengePrefix = ChatColor.WHITE + "[" + ChatColor.AQUA + "Challenges" + ChatColor.WHITE + "]";

    private static String positive = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "" + "+" + ChatColor.WHITE + "]";
    private static String info = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "" + "?" + ChatColor.WHITE + "]";
    private static String alert = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "" + "!" + ChatColor.WHITE + "]";

    public static void positiveChat(Player player, String message) {
        player.sendMessage(positive + ChatColor.GREEN + " " + message);
    }

    public static void alert(Player player, String message) {
        player.sendMessage(alert + ChatColor.RED +  " " + message);
    }

    public static void infoChat(Player player, String message) {
        player.sendMessage(info + message);
    }

    public static void positiveConsole(String message) {
        Bukkit.getLogger().info(prefix + ChatColor.GREEN + " " + message);
    }

    public static void alertConsole(String message) {
        Bukkit.getLogger().info(prefix + ChatColor.RED + " " + message);
    }

    public static void infoConsole(String message) {
        Bukkit.getLogger().info(prefix + ChatColor.YELLOW + " " + message);
    }

    public static void challengeChat(Player player, String message) {
        player.sendMessage(ChatColor.BOLD + "" + challengePrefix + ChatColor.RESET + "" + ChatColor.WHITE + " " + message);
    }

    public static void challengeBroadcast(String message) {
        Bukkit.broadcastMessage(challengePrefix + ChatColor.WHITE + " " + message);
    }

    public static net.md_5.bungee.api.ChatColor getColor(float num, float dem, String c) {
        int r = 10;
        int g = 10;
        int b = 10;

        float p = num/dem;

        if (c.equals("r")) r = Math.round(255*p);
        if (c.equals("g")) g = Math.round(255*p);
        if (c.equals("b")) b = Math.round(255*p);

        return net.md_5.bungee.api.ChatColor.of(new Color(r,g,b));
    }

    public static String getSuffix(int x) {
        String[] list = {
                "1st",  "2nd",  "3rd",  "4th",  "5th",  "6th",  "7th",  "8th",  "9th",
                "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th",
                "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th",
                "30th", "31st"
        };
        if (list.length > x) return list[x]; else return String.valueOf(x);

    }

}
