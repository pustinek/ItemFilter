package me.pustinek.itemfilter.commands;


import me.pustinek.itemfilter.ItemFilterPlugin;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Example of implemented command
 * */
public class CommandAdd extends CommandDefault {
    private final ItemFilterPlugin plugin;

    public CommandAdd(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter add";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.use")) {
            return "help_add";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player == null) {
            return;
        }
        if (!sender.hasPermission("itemfilter.add")) {
            ItemFilterPlugin.messageNoPrefix(sender, "no_perms");
            return;
        }


        plugin.getUserManager().getOrCreateUser(player.getUniqueId()).thenAccept(user -> {

            Material material;

            if(args.length > 1){
                if(args[1].equalsIgnoreCase("hand")){
                    material = player.getInventory().getItemInMainHand().getType();
                }else{
                    material = Material.getMaterial(args[1].toUpperCase());
                }
            }else{
                material = player.getInventory().getItemInMainHand().getType();
            }

            if(material == null){
                ItemFilterPlugin.message(sender, "invalid_material"," ");
                return;
            }else if(material == Material.AIR){
                ItemFilterPlugin.message(sender, "invalid_material", " ");
                return;
            }

            user.addMaterial(material);
            ItemFilterPlugin.message(sender, "filter_add", material.name());
        });


    }

    @Override
    public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
        List<String> result = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return result;
        }

        result.addAll(Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList()));

        return result;
    }
}
