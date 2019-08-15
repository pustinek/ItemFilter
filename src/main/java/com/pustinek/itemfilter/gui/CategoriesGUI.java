package com.pustinek.itemfilter.gui;

import com.pustinek.itemfilter.ItemFilter;
import com.pustinek.itemfilter.utils.NumberHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class CategoriesGUI {

    private final ItemFilter plugin;

    public CategoriesGUI(ItemFilter plugin) {
        this.plugin = plugin;

    }


    public void displayCategoriesGUI(Player player) {
        // Create inventory
        InventoryHolderGUI inventoryHolder = new InventoryHolderGUI(player);
        int guiSize = NumberHelper.nextMultipleOf9(plugin.getConfigManager().getCategoryMaxSlotSize());
        Inventory categoriesGUI = Bukkit.createInventory(inventoryHolder, guiSize, "Item Filter Categories");
        inventoryHolder.setInventory(categoriesGUI);

        // Fill the inventory with category items
        ItemStack spacer = plugin.getConfigManager().getSpacer();
        for (int i = 0; i < guiSize; i++) {
            categoriesGUI.setItem(i, spacer);
        }
        plugin.getConfigManager().getFilteredCategories().forEach(fi -> categoriesGUI.setItem(fi.getSlot(), fi.getItemStack()));
        player.openInventory(categoriesGUI);

    }


}
