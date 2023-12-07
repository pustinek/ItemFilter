package me.pustinek.itemfilter.commands;


import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
 * Example of implemented command
 * */
public class CommandRemove extends CommandDefault {
    private final ItemFilterPlugin plugin;

    public CommandRemove(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter remove";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.use")) {
            return "help_remove";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player == null) {
            return;
        }
        if (!sender.hasPermission("itemfilter.use")) {
            ItemFilterPlugin.messageNoPrefix(sender, "no_perms");
            return;
        }

        plugin.getUserManager().getOrCreateUser(player.getUniqueId()).thenAccept(user -> {
            ItemStack item = player.getInventory().getItemInMainHand();
            user.removeItem(item);
            ItemFilterPlugin.message(sender, "filter_remove", item.getItemMeta().getDisplayName());
        });


    }

}
