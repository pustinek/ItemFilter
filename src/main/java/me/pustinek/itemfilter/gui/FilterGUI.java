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
import me.pustinek.itemfilter.utils.FilterCategory;
import me.pustinek.itemfilter.utils.GUIUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FilterGUI implements InventoryProvider {

    final FilterCategory category;
    final User user;
    ClickableItem[] items;


    public FilterGUI(User user, FilterCategory category) {
        this.category = category;
        this.user = user;
        items = new ClickableItem[category.getFilterableMaterials().size()];
    }

    public static SmartInventory getSmartInventory(User user, FilterCategory category) {
        return SmartInventory.builder()
                .id("myInventory")
                .provider(new FilterGUI(user, category))
                .size(6, 9)
                .title(ChatUtils.chatColor(category.getTitle()))
                .manager(ItemFilterPlugin.getInstance().getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemFilterPlugin plugin = ItemFilterPlugin.getInstance();
        contents.fillRow(5, ClickableItem.empty(plugin.getConfigManager().getFilterGUISeperatorIcon()));

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(45);


        for (int i = 0; i < category.getFilterableMaterials().size(); i++) {
            Material material = category.getFilterableMaterials().get(i);
            ItemStack itemStackToShow;
            if (user.getMaterials().contains(material)) {
                itemStackToShow = GUIUtil.applyWillNotFilterToItem(plugin, new ItemStack(material));
            } else {
                itemStackToShow = GUIUtil.applyWillFilterToItem(plugin, new ItemStack(material));
            }

            this.items[i] = ClickableItem.of(itemStackToShow, e -> {
                user.toggleMaterial(material);
                init(player, contents);
            });
        }
        pagination.setItems(items);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0,0));


        // Back button
        contents.set(5,plugin.getConfigManager().getFilterGUIRelativeBackSlot(), ClickableItem.of(plugin.getConfigManager().getFilterGUIBackIcon(), e -> {
            CategoriesGUI.getSmartInventory(user).open(player);
        }));


        if(!pagination.isFirst()){
            contents.set(5, ItemFilterPlugin.getInstance().getConfigManager().getPreviousPageSlot(), ClickableItem.of(plugin.getConfigManager().getPreviousPageIcon(),
                    e -> getSmartInventory(user, category).open(player, pagination.previous().getPage())));
        }

        if(!pagination.isLast()){
            contents.set(5, ItemFilterPlugin.getInstance().getConfigManager().getNextPageSlot(), ClickableItem.of(plugin.getConfigManager().getNextPageIcon(),
                    e -> getSmartInventory(user, category).open(player, pagination.next().getPage())));
        }


    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}
