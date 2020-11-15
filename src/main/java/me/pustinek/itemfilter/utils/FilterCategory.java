package me.pustinek.itemfilter.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class FilterCategory {
    private String categoryID;
    private ItemStack itemStack;
    private Integer slot;
    private boolean enabled;
    private String title;
    private List<Material> filterableMaterials;

    public FilterCategory(String categoryID, ItemStack itemStack, Integer slot, boolean enabled, String title, List<Material> filterableItems) {
        this.categoryID = categoryID;
        this.itemStack = itemStack;
        this.slot = slot;
        this.enabled = enabled;
        this.title = title;
        this.filterableMaterials = filterableItems;
    }


}
