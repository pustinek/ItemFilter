package me.pustinek.itemfilter.commands;


import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Example of implemented command
 * */
public class CommandReset extends CommandDefault {
    private final ItemFilterPlugin plugin;

    public CommandReset(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter reset";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.use")) {
            return "help_reset";
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
            ItemFilterPlugin.messageNoPrefix(sender, "no_perms");
            return;
        }


        plugin.getUserManager().getOrCreateUser(player.getUniqueId()).thenAccept(user -> {
            user.resetMaterials();
            ItemFilterPlugin.message(sender, "reset_success");
        });

    }
}
