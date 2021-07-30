package io.github.spikey84.spikeyeco2;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private Material material;
    private String name;
    private List<String> lore;
    private int modelData = 0;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setModelData(int modelData) {
        this.modelData = modelData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        if (name != null) itemMeta.setDisplayName(name);
        if (lore != null) itemMeta.setLore(lore);
        if (modelData != 0) itemMeta.setCustomModelData(modelData);
        item.setItemMeta(itemMeta);
        return item;
    }
}
