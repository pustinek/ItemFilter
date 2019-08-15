package com.pustinek.itemfilter;

import org.bukkit.inventory.ItemStack;

public class FilterCategory {
    private String categoryID;
    private ItemStack itemStack;
    private Integer slot;
    private boolean enabled;
    private String title;
    private ItemStack[] filterableItems;

    public FilterCategory(String categoryID, ItemStack itemStack, Integer slot, boolean enabled, String title, ItemStack[] filterableItems) {
        this.categoryID = categoryID;
        this.itemStack = itemStack;
        this.slot = slot;
        this.enabled = enabled;
        this.title = title;
        this.filterableItems = filterableItems;
    }


    public Integer getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack[] getFilterableItems() {
        return filterableItems;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryID() {
        return categoryID;
    }
}
