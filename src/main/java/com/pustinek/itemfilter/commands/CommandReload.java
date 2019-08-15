package com.pustinek.itemfilter.commands;


import com.pustinek.itemfilter.ItemFilter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Example of implemented command
 * */
public class CommandReload extends CommandDefault {
    private final ItemFilter plugin;

    public CommandReload(ItemFilter plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter reload";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.reload")) {
            return "&2/itemfilter reload &f- reload configuration";
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
        if (!sender.hasPermission("itemfilter.reload")) {
            // The player (Sender) doesn't have the required permissions, notify him\
            ItemFilter.message(sender, "You don't have permission to execute that command");
            return;
        }

        plugin.getConfigManager().reloadConfig();


    }
}
