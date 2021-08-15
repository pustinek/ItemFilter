package me.pustinek.itemfilter.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.users.User;
import me.pustinek.itemfilter.utils.ChatUtils;
import me.pustinek.itemfilter.utils.FilterCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CategoriesGUI implements InventoryProvider {

    final User user;


    public CategoriesGUI(User user) {
        this.user = user;
    }

    public static SmartInventory getSmartInventory(User user) {
        return SmartInventory.builder()
                .id("myInventory")
                .provider(new CategoriesGUI(user))
                .size(Math.min(6, ItemFilterPlugin.getInstance().getConfigManager().getCategoriesGUIRowSize()), 9)
                .title(ChatUtils.chatColor(ItemFilterPlugin.getInstance().getConfig().getString("categoriesGUI.title", "Categories")))
                .manager(ItemFilterPlugin.getInstance().getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack spacer = ItemFilterPlugin.getInstance().getConfigManager().getSpacer();
        contents.fill(ClickableItem.empty(spacer));



        for (FilterCategory filteredCategory : ItemFilterPlugin.getInstance().getConfigManager().getFilteredCategories()) {
            contents.set(filteredCategory.getSlot() / 9, filteredCategory.getSlot() % 9, ClickableItem.of(filteredCategory.getItemStack(), e -> {
                FilterGUI.getSmartInventory(user, filteredCategory).open(player);
            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
