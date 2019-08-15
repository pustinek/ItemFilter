package com.pustinek.itemfilter.managers;

import com.pustinek.itemfilter.FilterCategory;
import com.pustinek.itemfilter.ItemFilter;
import com.pustinek.itemfilter.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends Manager {
    private final ItemFilter plugin;
    private FileConfiguration config;

    //Config variables
    private String pluginMessagePrefix = "&f[&cFilter&f] `";
    private ArrayList<FilterCategory> filteredCategories;
    private ItemStack spacer = null;
    private Boolean debug = false;


    private List<String> itemWillFilterLore = new ArrayList<>();
    private String itemWillFilterNamePrefix = "";
    private List<String> itemWillNotFilterLore = new ArrayList<>();
    private String itemWillNotFilterNamePrefix = "";

    private int categoryMaxSlotSize = 0;


    public ConfigManager(ItemFilter plugin) {

        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        try {

            //Create config file if it doesn't exist
            plugin.saveDefaultConfig();
            //Reload config
            plugin.reloadConfig();
            plugin.saveConfig();
            config = plugin.getConfig();

            //Start reading from config file
            loadDebugValue();
            loadConfig();

        } catch (Exception e) {
            //TODO: Handle error
            ItemFilter.debug(e.getMessage());
        }
    }

    private void loadDebugValue() {
        try {
            ItemFilter.isDebug = config.getBoolean("settings.debug");
        } catch (NullPointerException e) {
            ItemFilter.warning("failed to load debug value from config : " + e.getMessage());
        }

    }

    private void loadConfig() {
        if (config.contains("categoriesGUI") && config.contains("itemsGUI")) {
            ConfigurationSection spacersCS = config.getConfigurationSection("categoriesGUI.spacers");
            ConfigurationSection settingsCS = config.getConfigurationSection("settings");
            ConfigurationSection categoriesCS = config.getConfigurationSection("categoriesGUI.categories");
            ConfigurationSection itemsGUIFormatingCS = config.getConfigurationSection("itemsGUI.formating");

            //=== ItemsGUI configs
            if (itemsGUIFormatingCS != null) {
                itemWillFilterNamePrefix = itemsGUIFormatingCS.getString("will.prefix");
                itemWillFilterLore = itemsGUIFormatingCS.getStringList("will.lore");
                itemWillNotFilterNamePrefix = itemsGUIFormatingCS.getString("willNot.prefix");
                itemWillNotFilterLore = itemsGUIFormatingCS.getStringList("willNot.lore");

            } else {
                ItemFilter.warning("itemsGUI -> formating seems to be missing, check your config file");
            }


            //=== settings configs


            //=== Spacer configs
            if (spacersCS != null) {
                String matString = "";
                try {
                    matString = spacersCS.getString("material");
                    String name = spacersCS.getString("name");
                    List<String> lore = spacersCS.getStringList("lore");
                    Material material = Material.getMaterial(matString);
                    spacer = new ItemStack(material);
                    ItemMeta itemMeta = spacer.getItemMeta();
                    itemMeta.setDisplayName(name);
                    itemMeta.setLore(lore);
                    spacer.setItemMeta(itemMeta);
                } catch (Exception e) {
                    ItemFilter.warning("Spacer material [" + matString + "] is not valid, loading default");
                    spacer = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
                }

            } else {
                ItemFilter.warning("categoriesGUI -> spacers seems to be missing, check your config file");
            }

            //=== categories configs\
            filteredCategories = new ArrayList<>();
            for (String category : categoriesCS.getKeys(false)) {
                ItemFilter.debug("loading category - " + category);
                try {
                    String itemName = categoriesCS.getString(category + ".name");
                    String item = categoriesCS.getString(category + ".material");
                    int slot = categoriesCS.getInt(category + ".slot");
                    boolean enabled = categoriesCS.getBoolean(category + ".enabled");
                    String GUITitle = categoriesCS.getString(category + ".title");

                    if (slot > categoryMaxSlotSize) categoryMaxSlotSize = slot;

                    Material categoryMaterial = Material.getMaterial(item);
                    ItemStack categoryItemStack = new ItemStack(categoryMaterial);
                    ItemMeta im = categoryItemStack.getItemMeta();
                    im.setDisplayName(ColorUtil.chatColor(itemName));

                    categoryItemStack.setItemMeta(im);

                    ArrayList<ItemStack> filtrableItemStacks = new ArrayList<>();
                    for (String fi : categoriesCS.getStringList(category + ".items")) {
                        Material material = Material.getMaterial(fi);
                        ItemStack itemStack = new ItemStack(material);
                        filtrableItemStacks.add(itemStack);
                    }
                    ItemStack[] arrayed = filtrableItemStacks.toArray(new ItemStack[filtrableItemStacks.size()]);
                    FilterCategory filterCategory = new FilterCategory(category, categoryItemStack, slot, enabled, GUITitle, arrayed);


                    filteredCategories.add(filterCategory);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    ItemFilter.debug(e.getMessage());
                }

            }
        } else {
            ItemFilter.error("categoriesGUI or ItemsGUI section is missing, check your config file");
        }
    }


    public String getItemWillNotFilterNamePrefix() {
        return itemWillNotFilterNamePrefix;
    }

    public List<String> getItemWillFilterLore() {
        return itemWillFilterLore;
    }

    public String getItemWillFilterNamePrefix() {
        return itemWillFilterNamePrefix;
    }

    public List<String> getItemWillNotFilterLore() {
        return itemWillNotFilterLore;
    }

    public String getPluginMessagePrefix() {
        return pluginMessagePrefix;
    }

    public ItemStack getSpacer() {
        return spacer;
    }

    public ArrayList<FilterCategory> getFilteredCategories() {
        return filteredCategories;
    }


    public int getCategoryMaxSlotSize() {
        return categoryMaxSlotSize;
    }

    public Boolean isDebug() {
        return debug;
    }
}
