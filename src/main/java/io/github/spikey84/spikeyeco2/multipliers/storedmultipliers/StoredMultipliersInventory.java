package io.github.spikey84.spikeyeco2.multipliers.storedmultipliers;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.BaseInventory;
import io.github.spikey84.spikeyeco2.ItemBuilder;
import io.github.spikey84.spikeyeco2.multipliers.Multiplier;
import io.github.spikey84.spikeyeco2.multipliers.MultiplierManager;
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
import org.bukkit.map.MapPalette;
import org.bukkit.plugin.Plugin;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoredMultipliersInventory extends BaseInventory implements Listener {
    private Player player;
    private StoredMultiplierManager storedMultiplierManager;
    private int page;
    private Plugin plugin;
    private MultiplierManager multiplierManager;
    private int taskID;

    private final int[] slots = new int[]{10,11,12,13,14,15,16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    public StoredMultipliersInventory(Player player, StoredMultiplierManager storedMultiplierManager, Plugin plugin, MultiplierManager multiplierManager) {
        this(player, storedMultiplierManager, plugin,multiplierManager, 0);
    }

    private StoredMultipliersInventory(Player player, StoredMultiplierManager storedMultiplierManager, Plugin plugin, MultiplierManager multiplierManager, int page) {
        super(6, "Multipliers");
        this.plugin = plugin;
        this.page = page;
        this.player = player;
        this.storedMultiplierManager = storedMultiplierManager;
        this.multiplierManager = multiplierManager;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        Inventory tmp = this.getInventory();

        ItemBuilder filler = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        filler.setName(ChatColor.RESET + "");

        for (int i = 0; i < tmp.getSize(); i++) {
            tmp.setItem(i, filler.build());
        }

        List<Multiplier> storedMultipliers = storedMultiplierManager.getMultipliersByPlayer(player.getUniqueId());

        int x = 0;
        for(int i = page*slots.length; i < slots.length*(page+1);i++) {
            x = i - page*slots.length;

            if (i >= storedMultipliers.size()) {
                filler.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
                filler.setName("Empty Multiplier Slot");
                tmp.setItem(slots[x],filler.build());
                continue;
            }

            Multiplier multiplier = storedMultipliers.get(i);
            ItemBuilder itemBuilder = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            itemBuilder.setName(String.format("%sMultiplier: x%s for %s", ChatColor.WHITE, ChatColor.GREEN +  "" + multiplier.getMultiplier() + "" + ChatColor.WHITE,ChatColor.DARK_AQUA + "" + DurationFormatUtils.formatDuration(multiplier.getTime()*1000,"H:mm:ss", false) + "" + ChatColor.WHITE));
            ArrayList<String> tmpList = Lists.newArrayList();
            tmpList.add(ChatColor.GREEN + "[Click to Activate]");
            tmpList.add(ChatColor.GRAY + "ID: " + String.valueOf(multiplier.getID()));

            itemBuilder.setLore(tmpList);

            tmp.setItem(slots[x],itemBuilder.build());
        }

        if (slots.length + (page * slots.length) < storedMultipliers.size()) {
            ItemBuilder forward = new ItemBuilder(Material.ARROW);
            forward.setName(ChatColor.DARK_GRAY + "Next Page");
            tmp.setItem(52, forward.build());
        }

        if (page > 0) {
            ItemBuilder back = new ItemBuilder(Material.ARROW);
            back.setName(ChatColor.DARK_GRAY + "Last Page");
            tmp.setItem(46, back.build());
        }

        ItemBuilder current = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE);
        current.setName(ChatColor.WHITE + "Active Multiplier: ");
        if (!multiplierManager.hasMultiplier(player.getUniqueId())) {
            current.setMaterial(Material.RED_STAINED_GLASS_PANE);
            ArrayList<String> tmpList = Lists.newArrayList();
            tmpList.add(ChatColor.WHITE + "No Active Multiplier!");
            current.setLore(tmpList);
        } else {
            Multiplier currentMultiplier = multiplierManager.getMultiplier(player.getUniqueId());
            ArrayList<String> tmpList = Lists.newArrayList();
            tmpList.add(ChatColor.WHITE + "Multiplier: " + ChatColor.GREEN + "x" + currentMultiplier.getMultiplier());
            tmpList.add(ChatColor.WHITE + "Time Remaining: " + ChatColor.DARK_AQUA + DurationFormatUtils.formatDuration(currentMultiplier.getTime()*1000,"H:mm:ss", true));
            current.setLore(tmpList);
        }
        tmp.setItem(49, current.build());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        //if (event.getInventory().getItem(0) == null) return;
        //if (event.getInventory().getItem(0).getItemMeta().getCustomModelData().equals("1")) event.setCancelled(true); else return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().equals(this.getInventory())) event.setCancelled(true); else return;

        Inventory inventory = event.getClickedInventory();

        if (inventory.getItem(52).equals(event.getCurrentItem()) && event.getCurrentItem().getType().equals(Material.ARROW)) {
            player.openInventory(new StoredMultipliersInventory(player, storedMultiplierManager, plugin, multiplierManager, page+1).getInventory());
        }

        if (inventory.getItem(46).equals(event.getCurrentItem()) && event.getCurrentItem().getType().equals(Material.ARROW)) {
            player.openInventory(new StoredMultipliersInventory(player, storedMultiplierManager, plugin, multiplierManager, page-1).getInventory());
        }

        if (event.getCurrentItem().getItemMeta().lore() != null && event.getCurrentItem().getItemMeta().lore().get(1) != null) {
            for (Multiplier multiplier : storedMultiplierManager.getMultipliersByPlayer(player.getUniqueId())) {
                if (event.getCurrentItem().getItemMeta().lore().get(1).toString().contains(String.format("ID: %s", multiplier.getID()))) {
                    if (storedMultiplierManager.hasMultiplier(player.getUniqueId())) return;
                    storedMultiplierManager.activateMultiplier(multiplier);
                    player.openInventory(new StoredMultipliersInventory(player, storedMultiplierManager, plugin,multiplierManager, page).getInventory());
                }
            }
        }

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ItemStack item = this.getInventory().getItem(49);
            ItemMeta itemMeta = item.getItemMeta();
            ArrayList<String> tmpList = Lists.newArrayList();
            Multiplier currentMultiplier = multiplierManager.getMultiplier(player.getUniqueId());
            if (!multiplierManager.hasMultiplier(player.getUniqueId())) {
                tmpList.add(ChatColor.WHITE + "No Active Multiplier!");
                itemMeta.setLore(tmpList);
                item.setItemMeta(itemMeta);
                item.setType(Material.RED_STAINED_GLASS_PANE);
                return;
            }
            tmpList.add(ChatColor.WHITE + "Multiplier: " + ChatColor.GREEN + "x" + currentMultiplier.getMultiplier());
            tmpList.add(ChatColor.WHITE + "Time Remaining: " + ChatColor.DARK_AQUA + DurationFormatUtils.formatDuration(currentMultiplier.getTime()*1000,"H:mm:ss", true));
            itemMeta.setLore(tmpList);
            item.setItemMeta(itemMeta);
        }, 0 ,5);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
