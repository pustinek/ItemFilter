package com.pustinek.itemfilter.gui;

import com.pustinek.itemfilter.ItemFilter;
import com.pustinek.itemfilter.utils.ColorUtil;
import com.pustinek.itemfilter.utils.GUIUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class FilterGUI {

    private final ItemFilter plugin;

    public FilterGUI(ItemFilter plugin) {
        this.plugin = plugin;
    }


    //TODO: display filtered items based on player saved stuff

    /**
     * Display the filterable items inside a category page
     *
     * @param player          Player         being managed/managing
     * @param filterableItems All items defined under a category
     * @param title           Title of the page
     */
    public void displayFilterGUI(Player player, ItemStack[] filterableItems, String title, String categoryID) {
        InventoryHolderGUI inventoryHolder = new InventoryHolderGUI(0, player);
        inventoryHolder.setCategoryID(categoryID);
        int guiSize = 54;
        Inventory gui = Bukkit.createInventory(inventoryHolder, guiSize, ColorUtil.chatColor(title));

        ArrayList<Material> playerFilteredMaterials = plugin.getPlayerManager().getPlayerFilteredMaterials(player);

        for (int j = 0; j < filterableItems.length; j++) {
            ItemStack itemStackToShow;

            if (playerFilteredMaterials.contains(filterableItems[j].getType())) {
                itemStackToShow = GUIUtil.applyWillNotFilterToItem(plugin, filterableItems[j]);
            } else {
                itemStackToShow = GUIUtil.applyWillFilterToItem(plugin, filterableItems[j]);
            }
            gui.setItem(j, itemStackToShow);
        }

        inventoryHolder.setInventory(gui);
        player.openInventory(gui);
    }


}
