package com.pustinek.itemfilter.listeners;

import com.pustinek.itemfilter.ItemFilter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinListener implements Listener {

    private final ItemFilter plugin;

    public OnPlayerJoinListener(ItemFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        plugin.getPlayerManager().loadPlayerData(event.getPlayer());
    }

}
