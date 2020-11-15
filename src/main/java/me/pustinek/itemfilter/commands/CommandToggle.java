package me.pustinek.itemfilter.commands;


import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Example of implemented command
 * */
public class CommandToggle extends CommandDefault {
    private final ItemFilterPlugin plugin;

    public CommandToggle(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter toggle";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.use")) {
            return "help_toggle";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player == null) {
            return;
        }
        if (!sender.hasPermission("itemfilter.toggle")) {
            ItemFilterPlugin.messageNoPrefix(sender, "no_perms");
            return;
        }

        plugin.getUserManager().getOrCreateUser(player.getUniqueId()).thenAccept(user -> {
            if (!user.isEnabled()) {
                user.setEnabled(true);
                ItemFilterPlugin.message(sender, "filter_on");
            } else {
                user.setEnabled(false);
                ItemFilterPlugin.message(sender, "filter_off");
            }
        });

    }
}
