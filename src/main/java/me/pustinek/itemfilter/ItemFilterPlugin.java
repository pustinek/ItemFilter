package me.pustinek.itemfilter;

import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import me.pustinek.itemfilter.database.Database;
import me.pustinek.itemfilter.listeners.OnPlayerJoinListener;
import me.pustinek.itemfilter.listeners.OnPlayerPickupItemListener;
import me.pustinek.itemfilter.managers.CommandManager;
import me.pustinek.itemfilter.managers.ConfigManager;
import me.pustinek.itemfilter.managers.UserManager;
import me.pustinek.itemfilter.utils.ChatUtils;
import me.pustinek.itemfilter.utils.Manager;
import me.wiefferink.interactivemessenger.processing.Message;
import me.wiefferink.interactivemessenger.source.LanguageManager;
import org.apache.commons.lang.StringUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Getter
public final class ItemFilterPlugin extends JavaPlugin {

    // used by bStats for tracking
    private static final int PLUGIN_ID = 9401;

    private static Logger logger;

    private static ConfigManager configManager = null;
    private static CommandManager commandManager = null;
    private static Database database = null;
    // Managers:
    private Set<Manager> managers = new HashSet<>();
    private UserManager userManager;

    private InventoryManager inventoryManager;

    @Getter
    private static ItemFilterPlugin instance;

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
    public static void debug(String message, boolean force) {
        if (getInstance().getConfig().getBoolean("debug") || force)
            logger.info(ChatUtils.chatColor(message));
    }

    /**
     * Print a debug msg to the console.
     *
     * @param message The message to print
     */
    public static void debug(String message) {
        debug(message, false);
    }

    /**
     * Print an error to the console.
     *
     * @param message The message to print
     */
    public static void error(Object... message) {
        logger.severe(StringUtils.join(message, " "));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getUserManager().shutdown();
    }

    @Override
    public void onEnable() {
        // load logger
        instance = this;
        logger = this.getLogger();


        if(getConfig().getBoolean("metrics", true)){
            // bStats metrics setup:
            new Metrics(this, PLUGIN_ID);
        }


        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();



        if (configManager == null) {
            configManager = new ConfigManager(this);
            managers.add(configManager);
        }

        new LanguageManager(

                this,                                  // The plugin (used to get the languages bundled in the jar file)
                "languages",                           // Folder where the languages are stored
                getConfig().getString("language", "EN"),     // The language to use indicated by the plugin user
                "EN",                                  // The default language, expected to be shipped with the plugin and should be complete, fills in gaps in the user-selected language
                Collections.singletonList(getConfig().getString("prefix", "&7[&6ItemFilter&7] ")) // Chat prefix to use with Message#prefix(), could of course come from the config file
        );

        Message.useColorsInConsole(true);



        // Init and load database
        getDatabase();

        //load managers
        loadManagers();
        registerListeners();

        debug("&2=========", true);
        debug("ItemFilter", true);
        debug("Version: " + getDescription().getVersion(), true);
        debug("Authors: " + String.join(", ", getDescription().getAuthors()), true);
        debug("&2==========", true);


    }

    public void myReload() {
        configManager.reload();
        Message.useColorsInConsole(true);
        new LanguageManager(
                this,                                  // The plugin (used to get the languages bundled in the jar file)
                "languages",                           // Folder where the languages are stored
                getConfig().getString("language", "EN"),     // The language to use indicated by the plugin user
                "EN",                                  // The default language, expected to be shipped with the plugin and should be complete, fills in gaps in the user-selected language
                Collections.singletonList(getConfig().getString("prefix", "&7[&6ItemFilter&7] ")) // Chat prefix to use with Message#prefix(), could of course come from the config file
        );

    }

    private void loadManagers() {
        commandManager = new CommandManager(this);
        managers.add(commandManager);

        userManager = new UserManager(this);
        managers.add(userManager);
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new OnPlayerJoinListener(this), this);
        pm.registerEvents(new OnPlayerPickupItemListener(this), this);
    }

    public ConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = new ConfigManager(this);
            managers.add(configManager);
        }
        return configManager;

    }

    public UserManager getUserManager() {
        if (userManager == null) {
            userManager = new UserManager(this);
            managers.add(userManager);
        }
        return userManager;
    }

    public Database getDatabase() {
        if (database == null) {
            database = new Database(this);
            managers.add(database);
            database.load();
        }
        return database;
    }


    /**
     * Send a message to a target without a prefix.
     *
     * @param target       The target to send the message to
     * @param key          The key of the language string
     * @param replacements The replacements to insert in the message
     */
    public static void messageNoPrefix(Object target, String key, Object... replacements) {

        Bukkit.getScheduler().runTask(getInstance(), () -> Message.fromKey(key).replacements(replacements).send(target));

    }

    /**
     * Send a message to a target, prefixed by the default chat prefix.
     *
     * @param target       The target to send the message to
     * @param key          The key of the language string
     * @param replacements The replacements to insert in the message
     */
    public static void message(Object target, String key, Object... replacements) {
        Bukkit.getScheduler().runTask(getInstance(), () -> Message.fromKey(key).prefix().replacements(replacements).send(target));
    }
}
