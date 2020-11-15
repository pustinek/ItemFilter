package me.pustinek.itemfilter.managers;

import com.tchristofferson.configupdater.ConfigUpdater;
import lombok.Getter;
import lombok.NonNull;
import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.utils.FilterCategory;
import me.pustinek.itemfilter.utils.ItemBuilder;
import me.pustinek.itemfilter.utils.Manager;
import me.pustinek.itemfilter.utils.NumberHelper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class ConfigManager implements Manager {
    private FileConfiguration config;

    //Config variables
    private ArrayList<FilterCategory> filteredCategories;
    private ItemStack spacer = null;
    private Boolean debug = false;


    private List<String> itemWillFilterLore = new ArrayList<>();
    private String itemWillFilterNamePrefix = "";
    private List<String> itemWillNotFilterLore = new ArrayList<>();
    private String itemWillNotFilterNamePrefix = "";

    private int filterGUIRelativeBackSlot;
    private ItemStack filterGUIBackIcon;
    private int previousPageSlot;
    private int nextPageSlot;
    private ItemStack nextPageIcon;
    private ItemStack previousPageIcon;
    private ItemStack filterGUISeperatorIcon;

    private int categoriesGUIRowSize = 1;

    final ItemFilterPlugin plugin;
    public ConfigManager(ItemFilterPlugin plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, Arrays.asList("categoriesGUI.categories"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        reloadConfig();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {
        reloadConfig();
    }

    public void reloadConfig() {
        try {

            //Create config file if it doesn't exist
            plugin.saveDefaultConfig();
            //Reload config
            plugin.reloadConfig();
            config = plugin.getConfig();

            //Start reading from config file
            loadConfig();

        } catch (Exception e) {
            ItemFilterPlugin.error(e.getMessage());
        }
    }


    private void loadConfig() {
        if (config.contains("categoriesGUI") && config.contains("itemsGUI")) {
            ConfigurationSection spacersCS = config.getConfigurationSection("categoriesGUI.spacers");
            ConfigurationSection categoriesGUICS = config.getConfigurationSection("categoriesGUI");
            ConfigurationSection itemsGUIFormattingCS = config.getConfigurationSection("itemsGUI.formating");
            ConfigurationSection itemsGUICS = config.getConfigurationSection("itemsGUI");

            //=== ItemsGUI configs
            if (itemsGUICS == null) {
                ItemFilterPlugin.error("itemsGUI section is mission, disabling plugin...");
                plugin.onDisable();
                return;
            }

            filterGUIRelativeBackSlot = itemsGUICS.getInt("navigation.backButton.slot", 0);
            previousPageIcon = loadIconFromConfig(Objects.requireNonNull(itemsGUICS.getConfigurationSection("navigation.previous_page")), "&cPrevious Page", Material.PINK_DYE);
            nextPageIcon = loadIconFromConfig(Objects.requireNonNull(itemsGUICS.getConfigurationSection("navigation.next_page")), "&cPrevious Page", Material.LIME_DYE);
            filterGUIBackIcon = loadIconFromConfig(Objects.requireNonNull(itemsGUICS.getConfigurationSection("navigation.backButton")), "&cBack", Material.OAK_DOOR);
            previousPageSlot = itemsGUICS.getInt("navigation.previous_pag.slot", 4);
            nextPageSlot = itemsGUICS.getInt("navigation.next_page.slot", 4);
            filterGUISeperatorIcon = loadIconFromConfig(Objects.requireNonNull(itemsGUICS.getConfigurationSection("navigation.seperators")), " ", Material.BLACK_STAINED_GLASS_PANE);

            if (itemsGUIFormattingCS != null) {
                itemWillFilterNamePrefix = itemsGUIFormattingCS.getString("will.prefix");
                itemWillFilterLore = itemsGUIFormattingCS.getStringList("will.lore");
                itemWillNotFilterNamePrefix = itemsGUIFormattingCS.getString("willNot.prefix");
                itemWillNotFilterLore = itemsGUIFormattingCS.getStringList("willNot.lore");

            } else {
                ItemFilterPlugin.warning("itemsGUI -> formatting seems to be missing, check your config file");
            }


            //=== Spacer configs:
            if (spacersCS != null) {
                spacer = loadIconFromConfig(spacersCS, " ", Material.BLACK_STAINED_GLASS_PANE);
            } else {
                ItemFilterPlugin.warning("categoriesGUI -> spacers seems to be missing, check your config file");
            }

            //=== categories configs:
            final int categoriesGUISize = categoriesGUICS.getInt("rows", 1);
            categoriesGUIRowSize = NumberHelper.ensureRange(categoriesGUISize, 1, 5);

            filteredCategories = new ArrayList<>();
            ConfigurationSection categoriesCS = categoriesGUICS.getConfigurationSection("categories");
            for (String categoryKey : categoriesCS.getKeys(false)) {
                ItemFilterPlugin.debug("loading category - " + categoryKey);
                try {
                    ConfigurationSection categoryCS = categoriesCS.getConfigurationSection(categoryKey);
                    if (categoryCS == null) continue;

                    int slot = categoryCS.getInt("slot", 0);
                    boolean enabled = categoryCS.getBoolean("enabled", true);
                    String GUITitle = categoryCS.getString("title", categoryKey);

                    ItemStack categoryIcon = new ItemBuilder(loadIconFromConfig(categoryCS, "N/A", Material.BARRIER)).applyAllFlags().get();

                    ArrayList<Material> filterableMaterials = new ArrayList<>();
                    for (String fi : categoriesCS.getStringList(categoryKey + ".items")) {
                        Material material = Material.getMaterial(fi);
                        if (material != null)
                            filterableMaterials.add(material);
                    }
                    FilterCategory filterCategory = new FilterCategory(categoryKey, categoryIcon, slot, enabled, GUITitle, filterableMaterials);

                    filteredCategories.add(filterCategory);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    ItemFilterPlugin.debug(e.getMessage());
                }

            }
        } else {
            ItemFilterPlugin.error("categoriesGUI or ItemsGUI section is missing, check your config file");
        }
    }

    private ItemStack loadIconFromConfig(ConfigurationSection section, @NonNull String defName, @NonNull Material defMaterial) {
        String name = section.getString("name", defName);
        String materialName = section.getString("material", defMaterial.name());

        Material material = null;
        if (materialName != null)
            material = Material.getMaterial(materialName);

        if (material == null)
            material = defMaterial;

        List<String> lore = section.getStringList("lore");

        return new ItemBuilder().create(material, name, lore.toArray(new String[0])).get();
    }

}
