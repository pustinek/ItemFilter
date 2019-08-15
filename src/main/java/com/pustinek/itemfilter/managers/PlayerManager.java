package com.pustinek.itemfilter.managers;

import com.pustinek.itemfilter.ItemFilter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerManager extends Manager {

    private final ItemFilter plugin;
    private HashMap<Player, ArrayList<Material>> playerFilteredMaterials = new HashMap<>();
    private HashMap<Player, Boolean> playerPluginStatus = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public PlayerManager(ItemFilter plugin) {
        this.plugin = plugin;
        // Create folder for player saves
        new File(plugin.getDataFolder() + "/playerData").mkdirs();
    }


    // Load player data from filebase
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadPlayerData(Player player) {
        ItemFilter.debug("loading player...");
        String path = plugin.getDataFolder() + "/playerData/";
        String fileName = player.getUniqueId() + ".yml";

        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(path + fileName);
        YamlConfiguration config;
        try {
            if (file.createNewFile()) {
                config = YamlConfiguration.loadConfiguration(file);
                config.createSection("filteredItems");
                config.set("enabled", false);
                config.save(file);

                playerFilteredMaterials.put(player, new ArrayList<>());
                playerPluginStatus.put(player, false);
            } else {
                //File already exists
                config = YamlConfiguration.loadConfiguration(file);
                ArrayList<Material> materials = new ArrayList<>();
                boolean enabled = config.getBoolean("enabled");
                for (String fi : config.getStringList("filteredItems")) {
                    Material material = Material.getMaterial(fi);
                    materials.add(material);
                }

                playerFilteredMaterials.put(player, materials);
                playerPluginStatus.put(player, enabled);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: better error catching
            ItemFilter.error(e.getMessage());
            playerFilteredMaterials.put(player, new ArrayList<>());
            playerPluginStatus.put(player, false);
        }
    }

    public void savePlayerData(final Player player) throws IOException {
        File file = new File(plugin.getDataFolder() + "/playerData/" + player.getUniqueId() + ".yml");
        YamlConfiguration playerFile = YamlConfiguration.loadConfiguration(file);
        ArrayList<String> matNames = new ArrayList<>();
        getPlayerFilteredMaterials(player).forEach(fm -> matNames.add(fm.name()));

        playerFile.set("filteredItems", matNames);
        playerFile.set("enabled", isFilterEnabled(player));
        playerFile.save(file);
        // player is leaving so remove him from the hashmap
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!player.isOnline()) {
                playerFilteredMaterials.remove(player);
                playerPluginStatus.remove(player);
            }

        }, 40L);


    }

    //TODO: on disable plugin try and save all player stuff !!
    public void saveAllPlayerData() {
        playerFilteredMaterials.forEach((key, value) -> {
            try {
                savePlayerData(key);
            } catch (IOException e) {
                plugin.getLogger().warning("failed to save settings for player " + key.getName());
                e.printStackTrace();
            }
        });
    }

    /**
     * Display the filterable items inside a category page
     *
     * @param player   Player being managed
     * @param material Material to toggle
     * @return isBeingFiltered  item is being filtered (true) / item is not filtered (false)
     */
    public Boolean togglePlayerFilteredMaterial(Player player, Material material) {
        if (playerFilteredMaterials.get(player).contains(material)) {
            playerFilteredMaterials.get(player).remove(material);
            return false;
        } else {
            playerFilteredMaterials.get(player).add(material);
            return true;
        }
    }

    public Boolean togglePlayerPluginStatus(Player player) {
        playerPluginStatus.replace(player, !playerPluginStatus.get(player));
        return playerPluginStatus.get(player);
    }

    public Boolean isFilterEnabled(Player player) {
        return playerPluginStatus.get(player);
    }

    public Boolean isFiltered(Player player, Material material) {
        return playerFilteredMaterials.get(player).contains(material);
    }

    public ArrayList<Material> getPlayerFilteredMaterials(Player player) {
        return playerFilteredMaterials.get(player);
    }
}
