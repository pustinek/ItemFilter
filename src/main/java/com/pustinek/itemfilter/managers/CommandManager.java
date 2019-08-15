package com.pustinek.itemfilter.managers;

import com.pustinek.itemfilter.ItemFilter;
import com.pustinek.itemfilter.commands.CommandDefault;
import com.pustinek.itemfilter.commands.CommandEdit;
import com.pustinek.itemfilter.commands.CommandReload;
import com.pustinek.itemfilter.commands.CommandToggle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class CommandManager extends Manager implements CommandExecutor, TabCompleter {

    private final ArrayList<CommandDefault> commands;
    private ItemFilter plugin;

    public CommandManager(ItemFilter plugin) {

        commands = new ArrayList<>();
        //ADD: Here you add the commands that you want to use..
        commands.add(new CommandEdit(plugin));
        commands.add(new CommandReload(plugin));
        commands.add(new CommandToggle(plugin));

        plugin.getCommand("itemfilter").setExecutor(this);
        plugin.getCommand("itemfilter").setTabCompleter(this);


        // store plugin instance
        this.plugin = plugin;
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

        messages.add(0, "&2==== &fItemFilter " + plugin.getDescription().getVersion() + " &2====");

        messages.add("&2=================");


        for (String message : messages) {
            ItemFilter.messageNoPrefix(target, message);
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
            ItemFilter.message(sender, " Command is not valid");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (!sender.hasPermission("moneypouch.tabcomplete")) {
            return result;
        }
        int toCompleteNumber = args.length;
        String toCompletePrefix = args[args.length - 1].toLowerCase();


        if (toCompleteNumber == 1) {
            for (CommandDefault c : commands) {
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
}
