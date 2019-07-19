package com.pustinek.mctemplate;

import com.pustinek.mctemplate.listeners.ExampleListener;
import com.pustinek.mctemplate.managers.CommandManager;
import com.pustinek.mctemplate.managers.ConfigManager;
import com.pustinek.mctemplate.managers.Manager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

//TODO: change the plugin name
public final class McTemplate extends JavaPlugin {
    // Private static variables
    private static Logger logger;

    // Managers:
    private Set<Manager> managers = new HashSet<>();
    private ConfigManager configManager = null;
    private CommandManager commandManager = null;

    // General variables:


    @Override
    public void onEnable() {
        // load logger
        logger = this.getLogger();
        // Plugin startup logic

        //load managers
        loadManagers();
        registerListeners();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadManagers() {
        commandManager = new CommandManager(this);
        managers.add(commandManager);
        configManager = new ConfigManager(this);
        managers.add(configManager);
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ExampleListener(this), this);

    }
    /**
     * Print a warning to the console.
     * @param message The message to print
     */
    public static void warrning(String message) {
        logger.info(message);
    }
    /**
     * Print a debug msg to the console.
     * @param message The message to print
     */
    public static void debug(String message) {
         logger.info(message);
    }

    /**
     * Print an error to the console.
     * @param message The message to print
     */
    public static void error(Object... message) {
        logger.severe(StringUtils.join(message, " "));
    }


}
