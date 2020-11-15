package me.pustinek.itemfilter.listeners;

import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.users.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class OnPlayerPickupItemListener implements Listener {


    private final ItemFilterPlugin plugin;

    public OnPlayerPickupItemListener(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (!player.hasPermission("itemfilter.use")) return;

        Material material = event.getItem().getItemStack().getType();

        ItemStack item = event.getItem().getItemStack();

        Optional<User> user = plugin.getUserManager().getUser(player.getUniqueId());
        if (!user.isPresent()) return;
        if (!user.get().isEnabled()) return;
        if (!user.get().getMaterials().contains(material)) return;
        if(plugin.getConfig().getBoolean("ignore_custom_items", true)
                && item.getItemMeta() != null
                && (item.getItemMeta().hasDisplayName() || item.getItemMeta().hasLore() || item.getItemMeta().hasEnchants())
        )
            return;

        event.setCancelled(true);
    }

}
