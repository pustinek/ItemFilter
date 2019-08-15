package com.pustinek.itemfilter.listeners;

import com.pustinek.itemfilter.ItemFilter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class OnPlayerLeaveListener implements Listener {

    private final ItemFilter plugin;

    public OnPlayerLeaveListener(ItemFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        ItemFilter.debug("on player leave");
        try {
            plugin.getPlayerManager().savePlayerData(event.getPlayer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
