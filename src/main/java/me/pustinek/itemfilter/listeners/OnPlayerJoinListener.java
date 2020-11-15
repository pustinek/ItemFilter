package me.pustinek.itemfilter.listeners;

import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinListener implements Listener {

    private final ItemFilterPlugin plugin;

    public OnPlayerJoinListener(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getUserManager().loadUser(event.getPlayer());
    }

}
