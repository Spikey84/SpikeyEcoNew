package io.github.spikey84.spikeyeco2;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;


public abstract class BaseInventory implements InventoryHolder {
    private Inventory inventory;

    public BaseInventory(int rows, String title) {
        this.inventory = Bukkit.createInventory(this,9*rows, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
