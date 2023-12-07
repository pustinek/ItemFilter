package me.pustinek.itemfilter.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.users.User;
import me.pustinek.itemfilter.utils.ChatUtils;
import me.pustinek.itemfilter.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * FilteredGUI to display items
 * that will be filtered
 */
public class FilteredGUI implements InventoryProvider {
    final User user;
    final ClickableItem[] items;
    public FilteredGUI(User user) {
        this.user = user;
        items = new ClickableItem[this.user.getItems().size()];
    }

    public static SmartInventory getSmartInventory(User user) {
        return SmartInventory.builder()
                .id("myInventory")
                .provider(new FilteredGUI(user))
                .size(6, 9)
                .title(ChatUtils.chatColor("&6&lFiltered items"))
                .manager(ItemFilterPlugin.getInstance().getInventoryManager())
                .build();
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        ItemFilterPlugin plugin = ItemFilterPlugin.getInstance();


        contents.fillRow(5, ClickableItem.empty(plugin.getConfigManager().getFilterGUISeperatorIcon()));

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(45);


        for (int i = 0; i < user.getItems().size(); i++) {
            ItemStack item = user.getItems().get(i).clone();
            ItemBuilder itemBuilder = new ItemBuilder(item);
            itemBuilder.addLore("","&7------","&c&lClick to remove from filter");

            this.items[i] = ClickableItem.of(itemBuilder.get(), e -> {
                user.removeItem(item);
                e.setCurrentItem(null);
            });
        }

        pagination.setItems(items);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0,0));


        if(!pagination.isFirst()){
            contents.set(5, ItemFilterPlugin.getInstance().getConfigManager().getPreviousPageSlot(), ClickableItem.of(plugin.getConfigManager().getPreviousPageIcon(),
                    e -> getSmartInventory(user).open(player, pagination.previous().getPage())));
        }

        if(!pagination.isLast()){
            contents.set(5, ItemFilterPlugin.getInstance().getConfigManager().getNextPageSlot(), ClickableItem.of(plugin.getConfigManager().getNextPageIcon(),
                    e -> getSmartInventory(user).open(player, pagination.next().getPage())));
        }

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
