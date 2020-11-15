package me.pustinek.itemfilter.commands;


import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.command.CommandSender;

/*
 * Example of implemented command
 * */
public class CommandReload extends CommandDefault {
    private final ItemFilterPlugin plugin;

    public CommandReload(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter reload";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.reload")) {
            return "help_reload";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission("itemfilter.reload")) {
            ItemFilterPlugin.messageNoPrefix(sender, "no_perms");
            return;
        }

        plugin.myReload();
        ItemFilterPlugin.message(sender, "reload_success");

    }
}
