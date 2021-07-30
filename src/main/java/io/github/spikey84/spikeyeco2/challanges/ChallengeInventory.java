package io.github.spikey84.spikeyeco2.challanges;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.BaseInventory;
import io.github.spikey84.spikeyeco2.ItemBuilder;
import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultiplierManager;
import io.github.spikey84.spikeyeco2.multipliers.storedmultipliers.StoredMultipliersInventory;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ChallengeInventory extends BaseInventory implements Listener {
    private Player player;
    private Plugin plugin;
    private int taskID;
    private String time;
    private String price;



    public ChallengeInventory(Plugin plugin, Player player) {
        super(3, "Challenge");
        this.plugin = plugin;
        this.player = player;
        this.time = DurationFormatUtils.formatDuration((ChallangeManager.time + (ChallangeManager.chalStartTime -ChallangeManager.counter))*1000,"H:mm:ss", true);
        this.price = String.valueOf(ChallangeManager.currentPrice);

        Bukkit.getPluginManager().registerEvents(this, plugin);

        Inventory tmp = this.getInventory();

        ItemBuilder filler = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        filler.setName(ChatColor.RESET + "");

        for (int i = 0; i < tmp.getSize(); i++) {
            tmp.setItem(i, filler.build());
        }

        if(ChallangeManager.challengeProgress.get(player.getUniqueId()) != null) filler.setName(ChallangeManager.challengeProgress.get(player.getUniqueId()) + "/" + ChallangeManager.requiredPoints); else filler.setName("0/" + ChallangeManager.requiredPoints);
        filler.setMaterial(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 9; i++) {
            tmp.setItem(i, filler.build());
        }

        filler.setMaterial(Material.LIME_STAINED_GLASS_PANE);
//        filler.setName(ChatColor.WHITE + "Current Challenge:");
//        List<String> lore = Lists.newArrayList();
//        lore.add(String.format("To complete this challenge %s %s %s within %s for a %s reward.", ChallangeManager.currentChallenge.getChallengeClass().getAction(), ChallangeManager.requiredPoints, ChallangeManager.currentChallenge.getName(), ChallangeManager.timeStr, ChallangeManager.currentPrice));
//        filler.setLore(lore);
        if (ChallangeManager.challengeProgress.containsKey(player.getUniqueId())) {
            for (int i = 0; i < getFraction(ChallangeManager.challengeProgress.get(player.getUniqueId()), ChallangeManager.requiredPoints); i++) {
                tmp.setItem(i, filler.build());
            }
        }

        if (ChallangeManager.challengeComplete.contains(player.getUniqueId())) {
            ItemBuilder itemBuilder = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE);
            itemBuilder.setName("Current Challenge");
            List<String> lore = Lists.newArrayList();
            lore.add(String.format("To complete this challenge %s %ss %s within %s for %s coins.", ChallangeManager.currentChallenge.getChallengeClass().getAction(), ChallangeManager.requiredPoints, ChallangeManager.currentChallenge.getName(), time, price));
            lore.add(String.format(ChatColor.WHITE + "Reward: %s", ChatColor.GREEN + "" + price));
            lore.add(ChatColor.BOLD + "" + ChatColor.GREEN + "COMPLETE");
            itemBuilder.setLore(lore);
            tmp.setItem(22, itemBuilder.build());
            return;
        }
        ItemBuilder itemBuilder = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE);
        itemBuilder.setName("Current Challenge");
        List<String> lore = Lists.newArrayList();
        lore.add(String.format(ChatColor.WHITE + "To complete this challenge %s %s %ss within %s.", ChallangeManager.currentChallenge.getChallengeClass().getAction(), ChallangeManager.requiredPoints, ChallangeManager.currentChallenge.getName(), time, price));
        lore.add(String.format(ChatColor.WHITE + "Reward: %s", ChatColor.GREEN + "" + price));
        itemBuilder.setLore(lore);
        tmp.setItem(22, itemBuilder.build());




    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        //if (event.getInventory().getItem(0) == null) return;
        //if (event.getInventory().getItem(0).getItemMeta().getCustomModelData().equals("1")) event.setCancelled(true); else return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().equals(this.getInventory())) event.setCancelled(true); else return;

        Inventory inventory =  event.getClickedInventory();

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            this.time = DurationFormatUtils.formatDuration((ChallangeManager.time + (ChallangeManager.chalStartTime -ChallangeManager.counter))*1000,"H:mm:ss", true);
            this.price = String.valueOf(ChallangeManager.currentPrice);

            if (ChallangeManager.challengeComplete.contains(player.getUniqueId())) {
                ItemBuilder itemBuilder = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE);
                itemBuilder.setName("Current Challenge");
                List<String> lore = Lists.newArrayList();
                lore.add(String.format("To complete this challenge %s %ss %s within %s for %s coins.", ChallangeManager.currentChallenge.getChallengeClass().getAction(), ChallangeManager.requiredPoints, ChallangeManager.currentChallenge.getName(), time, price));
                lore.add(String.format(ChatColor.WHITE + "Reward: %s", ChatColor.GREEN + "" +price));
                lore.add(ChatColor.BOLD + "" + ChatColor.GREEN + "COMPLETE");
                itemBuilder.setLore(lore);
                this.getInventory().setItem(22, itemBuilder.build());
                return;
            }
            ItemBuilder itemBuilder = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE);
            itemBuilder.setName("Current Challenge");
            List<String> lore = Lists.newArrayList();
            lore.add(String.format(ChatColor.WHITE + "To complete this challenge %s %s %ss within %s.", ChallangeManager.currentChallenge.getChallengeClass().getAction(), ChallangeManager.requiredPoints, ChallangeManager.currentChallenge.getName(), time, price));
            lore.add(String.format(ChatColor.WHITE + "Reward: %s", ChatColor.GREEN + "" +price));
            itemBuilder.setLore(lore);
            this.getInventory().setItem(22, itemBuilder.build());
        }, 0 ,5);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public int getFraction(float num, float den) {
        float p = num/den;

        return Math.round(9*p);
    }
}
