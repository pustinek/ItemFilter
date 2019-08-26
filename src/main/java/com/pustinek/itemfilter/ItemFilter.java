package com.pustinek.itemfilter;

import com.pustinek.itemfilter.gui.CategoriesGUI;
import com.pustinek.itemfilter.gui.FilterGUI;
import com.pustinek.itemfilter.listeners.InventoryListener;
import com.pustinek.itemfilter.listeners.OnPlayerJoinListener;
import com.pustinek.itemfilter.listeners.OnPlayerLeaveListener;
import com.pustinek.itemfilter.listeners.OnPlayerPickupItemListener;
import com.pustinek.itemfilter.managers.CommandManager;
import com.pustinek.itemfilter.managers.ConfigManager;
import com.pustinek.itemfilter.managers.Manager;
import com.pustinek.itemfilter.managers.PlayerManager;
import com.pustinek.itemfilter.utils.ColorUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

//TODO: change the plugin name
public final class ItemFilter extends JavaPlugin {

    // Private static variables
    public static Boolean isDebug = false;
    private static Logger logger;
    private static ItemFilter instance;


    // Managers:
    private Set<Manager> managers = new HashSet<>();
    private static ConfigManager configManager = null;
    private CommandManager commandManager = null;
    private CategoriesGUI categoriesGUI;
    private FilterGUI filterGUI;
    private PlayerManager playerManager;

    // General variables:

    public static void messageNoPrefix(CommandSender sender, String message) {
        sender.sendMessage(ColorUtil.chatColor(message));
    }

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(ColorUtil.chatColor(configManager.getPluginMessagePrefix()) + ColorUtil.chatColor(message));
    }

    /**
     * Print a warning to the console.
     *
     * @param message The message to print
     */
    public static void warning(String message) {
        logger.warning(message);
    }

    /**
     * Print a debug msg to the console.
     *
     * @param message The message to print
     */
    public static void debug(String message) {
        if (isDebug)
            logger.info(message);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onEnable() {
        // load logger

        logger = this.getLogger();
        if (configManager == null) {
            configManager = new ConfigManager(this);
            managers.add(configManager);
            logger.info("loaded config manager");
        }

        //load managers
        loadGUI();
        loadManagers();
        registerListeners();

    }

    private void loadGUI() {
        filterGUI = new FilterGUI(this);
        categoriesGUI = new CategoriesGUI(this);
    }

    private void loadManagers() {


        if (commandManager == null) {
            commandManager = new CommandManager(this);
            managers.add(commandManager);
        }
        if (playerManager == null) {
            playerManager = new PlayerManager(this);
            managers.add(playerManager);
        }
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new OnPlayerJoinListener(this), this);
        pm.registerEvents(new OnPlayerLeaveListener(this), this);
        pm.registerEvents(new OnPlayerPickupItemListener(this), this);
        pm.registerEvents(new InventoryListener(this, categoriesGUI, filterGUI), this);

    }

    public ConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = new ConfigManager(this);
            managers.add(configManager);
        }
        return configManager;

    }

    public PlayerManager getPlayerManager() {
        if (playerManager == null) {
            playerManager = new PlayerManager(this);
            managers.add(playerManager);
        }
        return playerManager;
    }

    public FilterGUI getFilterGUI() {
        return filterGUI;
    }

    public CategoriesGUI getCategoriesGUI() {
        return categoriesGUI;
    }

    /**
     * Print an error to the console.
     * @param message The message to print
     */
    public static void error(Object... message) {
        logger.severe(StringUtils.join(message, " "));
    }


}
