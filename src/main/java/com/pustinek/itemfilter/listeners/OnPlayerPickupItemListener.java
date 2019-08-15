package com.pustinek.itemfilter.listeners;

import com.pustinek.itemfilter.ItemFilter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class OnPlayerPickupItemListener implements Listener {


    private final ItemFilter plugin;

    public OnPlayerPickupItemListener(ItemFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        Material material = event.getItem().getItemStack().getType();

        if (!plugin.getPlayerManager().isFilterEnabled(player)) return;
        if (plugin.getPlayerManager().isFiltered(player, material)) event.setCancelled(true);
    }

}
