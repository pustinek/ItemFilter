package me.pustinek.itemfilter.utils;

import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIUtil {


    public static ItemStack applyWillFilterToItem(ItemFilterPlugin plugin, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            ItemFilterPlugin.warning("Item meta is null !");
        } else {
            itemMeta.setDisplayName(ChatUtils.chatColor(plugin.getConfigManager().getItemWillFilterNamePrefix() + item.getType().name()));
            itemMeta.setLore(ChatUtils.chatColor(plugin.getConfigManager().getItemWillFilterLore()));
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
        item.removeEnchantment(Enchantment.DURABILITY);
        return item;
    }

    public static ItemStack applyWillNotFilterToItem(ItemFilterPlugin plugin, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            ItemFilterPlugin.warning("Item meta is null !");
        } else {
            itemMeta.setDisplayName(ChatUtils.chatColor(plugin.getConfigManager().getItemWillNotFilterNamePrefix() + item.getType().name()));
            itemMeta.setLore(ChatUtils.chatColor(plugin.getConfigManager().getItemWillNotFilterLore()));
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        return item;
    }
}
