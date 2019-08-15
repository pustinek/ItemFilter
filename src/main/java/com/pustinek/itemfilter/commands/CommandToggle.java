package com.pustinek.itemfilter.commands;


import com.pustinek.itemfilter.ItemFilter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Example of implemented command
 * */
public class CommandToggle extends CommandDefault {
    private final ItemFilter plugin;

    public CommandToggle(ItemFilter plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter toggle";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.use")) {
            return "&2/itemfilter toggle &f- toggle filter";
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
        if (!sender.hasPermission("itemfilter.use")) {
            // The player (Sender) doesn't have the required permissions, notify him\
            ItemFilter.message(sender, "You don't have permission to execute that command");
            return;
        }

        if (plugin.getPlayerManager().togglePlayerPluginStatus(player)) {
            ItemFilter.message(sender, "filter is &2ON");
        } else {
            ItemFilter.message(sender, "filter is &4OFF");
        }


    }
}
