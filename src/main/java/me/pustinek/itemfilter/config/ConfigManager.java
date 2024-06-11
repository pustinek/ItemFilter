package me.pustinek.itemfilter.config;

import com.tchristofferson.configupdater.ConfigUpdater;
import lombok.Getter;
import lombok.NonNull;
import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.utils.FilterCategory;
import me.pustinek.itemfilter.utils.ItemBuilder;
import me.pustinek.itemfilter.utils.Manager;
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

    private Boolean debug = false;

    private int filterGUIRelativeBackSlot;
    private ItemStack filterGUIBackIcon;
    private int previousPageSlot;
    private int nextPageSlot;
    private ItemStack nextPageIcon;
    private ItemStack previousPageIcon;
    private ItemStack filterGUISeperatorIcon;

    final ItemFilterPlugin plugin;
    public ConfigManager(ItemFilterPlugin plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(plugin, "config.yml", configFile);
        } catch (IOException e) {
            ItemFilterPlugin.error("Failed to update config file: " + e.getMessage());
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
        if (config.contains("itemsGUI")) {
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
        } else {
            ItemFilterPlugin.error("ItemsGUI section is missing, check your config file");
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
