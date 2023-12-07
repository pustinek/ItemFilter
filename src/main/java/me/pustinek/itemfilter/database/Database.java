package me.pustinek.itemfilter.database;

import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.users.User;
import me.pustinek.itemfilter.utils.BukkitSerialization;
import me.pustinek.itemfilter.utils.Manager;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Database implements Manager {


    final String dbFileName = "data";
    final String tablePlayers = "players";
    final ItemFilterPlugin plugin;
    public Database(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }


    Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbFileName + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                ItemFilterPlugin.error("File write error: " + dbFileName + ".db");
            }
        }

        try {
            Connection connection;
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            ItemFilterPlugin.error("SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            ItemFilterPlugin.error("You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    private void initialize() {

        try (
                Connection connection = getSQLConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tablePlayers + " WHERE uuid = 1")
        ) {
            ps.executeQuery();
            ItemFilterPlugin.debug("Successfully made a connection with database");
        } catch (SQLException ex) {
            ItemFilterPlugin.error("Unable to retrieve connection", ex);
        }
    }

    public void load() {
        try (Connection connection = getSQLConnection()) {
            try (Statement s = connection.createStatement()) {
                s.executeUpdate(getQueryData());
            }
        } catch (SQLException e) {
            ItemFilterPlugin.error("Failed to initialize or connect to database");
            e.printStackTrace();
        }
        initialize();
    }


    public CompletableFuture<User> getUser(UUID playerUUID) {

        String query = "SELECT * FROM " + tablePlayers + " WHERE uuid=?";
        try (
                Connection con = getSQLConnection();
                PreparedStatement ps = con.prepareStatement(query)

        ) {

            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            boolean next = rs.next();
            if (next) {
                boolean enabled = rs.getBoolean("enabled");
                String itemsAsString = rs.getString("items");


                ItemStack[] itemStacks;
                try {
                    itemStacks = BukkitSerialization.itemStackArrayFromBase64(itemsAsString);
                } catch (IOException e) {
                    e.printStackTrace();
                    return CompletableFuture.supplyAsync(() -> null);
                }
                // itemstacks to arraylist
                ArrayList<ItemStack> itemStackList = new ArrayList<>(Arrays.asList(itemStacks));

                User user = new User(playerUUID, enabled, itemStackList);
                return CompletableFuture.supplyAsync(() -> user);
            }

        } catch (SQLException ex) {
            ItemFilterPlugin.error("Failed getting user from database ", ex.getMessage());
            ex.printStackTrace();
        }

        return CompletableFuture.supplyAsync(() -> null);
    }

    public boolean saveUser(User user) {
        String query = "INSERT OR REPLACE INTO " + tablePlayers + " (uuid, enabled, items) VALUES(?,?,?)";

        try (
                Connection con = getSQLConnection();
                PreparedStatement ps = con.prepareStatement(query)
        ) {
            ps.setString(1, user.getUuid().toString());
            ps.setBoolean(2, user.isEnabled());
            ps.setString(3, BukkitSerialization.itemStackArrayToBase64(user.getItems().toArray(new ItemStack[0])));
            int i = ps.executeUpdate();
            return i > 0;
        } catch (SQLException ex) {
            ItemFilterPlugin.error("Failed saving user to database ", ex.getMessage());
            return false;
        }
    }


    private String getQueryData() {
        return "CREATE TABLE IF NOT EXISTS " + tablePlayers + " ("
                + "uuid VARCHAR(36) PRIMARY KEY NOT NULL,"
                + "enabled INTEGER NOT NULL,"
                + "items TEXT NOT NULL)";
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }
}
