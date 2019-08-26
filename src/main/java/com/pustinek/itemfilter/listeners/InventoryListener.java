package com.pustinek.itemfilter.listeners;

import com.pustinek.itemfilter.ItemFilter;
import com.pustinek.itemfilter.gui.CategoriesGUI;
import com.pustinek.itemfilter.gui.FilterGUI;
import com.pustinek.itemfilter.gui.InventoryHolderGUI;
import com.pustinek.itemfilter.utils.GUIUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {


    private final ItemFilter plugin;
    private final CategoriesGUI categoriesGUI;
    private final FilterGUI filterGUI;

    public InventoryListener(ItemFilter plugin, CategoriesGUI categoriesGUI, FilterGUI filterGUI) {
        this.plugin = plugin;
        this.categoriesGUI = categoriesGUI;
        this.filterGUI = filterGUI;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof InventoryHolderGUI) || event.getRawSlot() < 0) {
            return;
        }

        // Prevent players from taking items out of the GUI.
        event.setCancelled(true);
        // Clicking empty slots should do nothing
        if (event.getCurrentItem() == null) {
            return;
        }
        // Prevent user from clicking items in his own inventory (otherwise it will brake the item)
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            return;
        }



        int currentPage = ((InventoryHolderGUI) inventory.getHolder()).getPageIndex();
        Player player = (Player) event.getWhoClicked();

        //Categories GUI click happened
        if (currentPage == -1) {
            //TODO: check for item clicked and display appropriate filter GUI\
            plugin.getConfigManager().getFilteredCategories().forEach(fi -> {
                if (fi.getSlot() == event.getRawSlot()) {
                    //Display filtered category
                    filterGUI.displayFilterGUI(player, fi.getFilterableItems(), fi.getTitle(), fi.getCategoryID());
                }
            });
        }
        if (currentPage != -1) {
            if (plugin.getPlayerManager().togglePlayerFilteredMaterial(player, event.getCurrentItem().getType())) {
                ItemFilter.debug("Apply will filter");
                event.setCurrentItem(GUIUtil.applyWillNotFilterToItem(plugin, event.getCurrentItem()));
            } else {
                ItemFilter.debug("Apply will NOT filter");
                event.setCurrentItem(GUIUtil.applyWillFilterToItem(plugin, event.getCurrentItem()));
            }

        }


    }

}
