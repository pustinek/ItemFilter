package me.pustinek.itemfilter.users;

import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.users.User;
import me.pustinek.itemfilter.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager implements Manager {


    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    private final BukkitTask savingTask;

    private final ItemFilterPlugin plugin;

    public UserManager(ItemFilterPlugin plugin) {
        this.plugin = plugin;

        this.savingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            users.values().forEach(user -> {
                if (user.isDirty()) {
                    plugin.getDatabase().saveUser(user);
                    user.setDirty(false);
                }
            });
        }, 20 * 60, 20 * 60);
    }

    @Override
    public void shutdown() {
        this.savingTask.cancel();

        users.values().forEach(user -> {
            if (user.isDirty()) {
                plugin.getDatabase().saveUser(user);
                user.setDirty(false);
            }
        });

        users.clear();
    }

    @Override
    public void reload() {

    }

    public Optional<User> getUser(UUID playerUUID) {
        return Optional.ofNullable(users.get(playerUUID));
    }


    /*
    * Get user object in sequence:
    * memory -> SQLite
    * if not found create the object and return in
    * */
    public CompletableFuture<User> getOrCreateUser(UUID playerUUID){
        if(users.containsKey(playerUUID)) return CompletableFuture.supplyAsync(() -> users.get(playerUUID));
        return plugin.getDatabase().getUser(playerUUID).thenCompose(user -> {
            if (user == null) {
               return CompletableFuture.supplyAsync(() -> createUser(playerUUID));
                //return createUser(playerUUID);
            } else {
                users.put(playerUUID, user);
                return CompletableFuture.supplyAsync(() -> user);
            }
        });
    }

    public User createUser(UUID playerUUID) {
        ItemFilterPlugin.debug("Creating user " + playerUUID);
        User user = new User(playerUUID, false, new ArrayList<>());
        users.put(playerUUID, user);
        return user;
    }


    public void loadUser(Player player){
        if(users.containsKey(player.getUniqueId())) return;
        // Load user from database
        plugin.getDatabase().getUser(player.getUniqueId()).thenAccept(user -> {
            if (user != null) {
                users.put(user.getUuid(),user);
            }
        });
    }




}
