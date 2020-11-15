package me.pustinek.itemfilter.commands;


import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.gui.CategoriesGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Example of implemented command
 * */
public class CommandMenu extends CommandDefault {
    private final ItemFilterPlugin plugin;

    public CommandMenu(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter menu";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.menu")) {
            return "help_menu";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player == null) {
            // Command was triggered via the console,
            return;
        }
        if (!sender.hasPermission("itemfilter.menu")) {
            // The player (Sender) doesn't have the required permissions, notify him\
            ItemFilterPlugin.messageNoPrefix(sender, "no_perms");
            return;
        }


        ItemFilterPlugin.getInstance().getUserManager().getOrCreateUser(player.getUniqueId()).thenAccept(user -> {
            Bukkit.getScheduler().runTask(ItemFilterPlugin.getInstance(), () ->  CategoriesGUI.getSmartInventory(user).open(player));

        });


    }
}
