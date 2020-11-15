package me.pustinek.itemfilter.managers;

import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.commands.*;
import me.pustinek.itemfilter.utils.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class CommandManager implements Manager,CommandExecutor, TabCompleter {

    private final ArrayList<CommandDefault> commands;
    final ItemFilterPlugin plugin;
    public CommandManager(ItemFilterPlugin plugin) {
        this.plugin = plugin;

        commands = new ArrayList<>();
        commands.add(new CommandReload(plugin));
        commands.add(new CommandToggle(plugin));
        commands.add(new CommandMenu(plugin));
        commands.add(new CommandReset(plugin));
        commands.add(new CommandAdd(plugin));
        commands.add(new CommandRemove(plugin));

        plugin.getCommand("itemfilter").setExecutor(this);
        plugin.getCommand("itemfilter").setTabCompleter(this);

    }

    private void showHelp(CommandSender target) {

        // Add all messages to a list
        ArrayList<String> messages = new ArrayList<>();


        for (CommandDefault command : commands) {
            String help = command.getHelp(target);
            if (help != null && help.length() != 0) {
                messages.add(help);
            }
        }

        if(!messages.isEmpty()){
            messages.add(0, "help_header");
            messages.add("help_footer");
        }


        for (String message : messages) {
            ItemFilterPlugin.messageNoPrefix(target, message);
        }


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean executed = false;
        for (int i = 0; i < commands.size() && !executed; i++) {
            if (commands.get(i).canExecute(command, args)) {
                commands.get(i).execute(sender, args);
                executed = true;
            }
        }
        if (!executed && args.length == 0) {
            this.showHelp(sender);
        } else if (!executed) {
            ItemFilterPlugin.messageNoPrefix(sender, "invalid_command");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (!sender.hasPermission("itemfilter.use")) {
            return result;
        }
        int toCompleteNumber = args.length;
        String toCompletePrefix = args[args.length - 1].toLowerCase();


        if (toCompleteNumber == 1) {
            for (CommandDefault c : commands) {
                if(c.getHelp(sender) == null) continue;
                String begin = c.getCommandStart();
                result.add(begin.substring(begin.indexOf(' ') + 1));
            }
        } else {
            String[] start = new String[args.length];
            start[0] = command.getName();
            System.arraycopy(args, 0, start, 1, args.length - 1);
            for (CommandDefault c : commands) {
                if (c.canExecute(command, args)) {
                    result = c.getTabCompleteList(toCompleteNumber, start, sender);
                }
            }
        }
        // Filter and sort the results
        if (!result.isEmpty()) {
            SortedSet<String> set = new TreeSet<>();
            for (String suggestion : result) {
                if (suggestion.toLowerCase().startsWith(toCompletePrefix)) {
                    set.add(suggestion);
                }
            }
            result.clear();
            result.addAll(set);
        }
        return result;

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }
}
