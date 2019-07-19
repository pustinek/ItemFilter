package com.pustinek.mctemplate.commands;


import com.pustinek.mctemplate.McTemplate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
* Example of implemented command
* */
public class CommandExample extends CommandDefault {
    private final McTemplate plugin;

    public CommandExample(McTemplate plugin){
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "mctemplate example";
    }

    @Override
    public String getHelp(CommandSender target) {
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(player == null) {
            // Command was triggered via the console,
            return;
        }
        if(!sender.hasPermission("plugin.example")){
            // The player (Sender) doesn't have the required permissions, notify him
            return;
        }


    }
}
