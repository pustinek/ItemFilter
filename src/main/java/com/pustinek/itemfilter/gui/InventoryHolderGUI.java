package com.pustinek.itemfilter.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryHolderGUI implements InventoryHolder {
    private static final int MAIN_GUI_PAGE = -1;
    private final int pageIndex;
    private final Player player;
    private String categoryID;
    private Inventory inventory;

    InventoryHolderGUI(Player player) {
        this.player = player;
        this.pageIndex = MAIN_GUI_PAGE;
    }

    InventoryHolderGUI(int pageIndex, Player player) {
        this.player = player;
        this.pageIndex = pageIndex;
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public String getCategoryID() {
        return categoryID;
    }

    void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
