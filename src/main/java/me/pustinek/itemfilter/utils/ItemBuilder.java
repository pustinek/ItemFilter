package me.pustinek.itemfilter.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {


    private ItemStack itemStack;


    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder() {

    }

    public ItemStack get() {
        return itemStack;
    }

    public ItemBuilder create(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(ChatUtils.chatColor(name));

        // Set the lore of the item
        if (lore != null && lore.length > 0) {
            meta.setLore(ChatUtils.chatColor(Arrays.asList(lore)));
        }

        item.setItemMeta(meta);


        itemStack = item;

        return this;
    }


    public ItemBuilder applyEnchant(final Enchantment enchantment, int level, boolean ignoreRestrictions) {

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return this;
        itemMeta.addEnchant(enchantment, level, ignoreRestrictions);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeEnchants() {

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return this;
        itemMeta.getEnchants().forEach((enchantment, integer) -> {
            itemMeta.removeEnchant(enchantment);
        });
        itemStack.setItemMeta(itemMeta);
        return this;
    }


    public ItemBuilder applyFlag(final ItemFlag flag) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return this;
        itemMeta.addItemFlags(flag);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder applyAllFlags() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return this;
        for (ItemFlag flag : ItemFlag.values()) {
            itemMeta.addItemFlags(flag);
        }
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(int value) {
        if (value < 1) value = 1;
        if (value > 64) value = 64;
        itemStack.setAmount(value);
        return this;
    }


    public ItemBuilder addLore(final String... lore) {
        final ItemMeta meta = itemStack.getItemMeta();

        // Set the lore of the item
        if (lore != null && lore.length > 0 && meta != null) {
            meta.setLore(ChatUtils.chatColor(Arrays.asList(lore)));
        }

        itemStack.setItemMeta(meta);
        return this;
    }

}
