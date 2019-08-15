package com.pustinek.itemfilter.utils;

import com.pustinek.itemfilter.ItemFilter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIUtil {


    public static ItemStack applyWillFilterToItem(ItemFilter plugin, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            ItemFilter.warning("Item meta is null !");
        } else {
            itemMeta.setDisplayName(ColorUtil.chatColor(plugin.getConfigManager().getItemWillFilterNamePrefix() + item.getType().name()));
            itemMeta.setLore(ColorUtil.chatColor(plugin.getConfigManager().getItemWillFilterLore()));
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
        item.removeEnchantment(Enchantment.DURABILITY);
        return item;
    }

    public static ItemStack applyWillNotFilterToItem(ItemFilter plugin, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            ItemFilter.warning("Item meta is null !");
        } else {
            itemMeta.setDisplayName(ColorUtil.chatColor(plugin.getConfigManager().getItemWillNotFilterNamePrefix() + item.getType().name()));
            itemMeta.setLore(ColorUtil.chatColor(plugin.getConfigManager().getItemWillNotFilterLore()));
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        return item;
    }
}
