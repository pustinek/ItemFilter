package me.pustinek.itemfilter.commands;


import me.pustinek.itemfilter.ItemFilterPlugin;
import me.pustinek.itemfilter.users.User;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Example of implemented command
 * */
public class CommandRemove extends CommandDefault {
    private final ItemFilterPlugin plugin;

    public CommandRemove(ItemFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommandStart() {
        return "itemfilter remove";
    }

    @Override
    public String getHelp(CommandSender target) {
        if (target.hasPermission("itemfilter.remove")) {
            return "help_remove";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player == null) {
            return;
        }
        if (!sender.hasPermission("itemfilter.use")) {
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
                ItemFilterPlugin.message(sender, "invalid_material","Null");
                return;
            }else if(material == Material.AIR){
                ItemFilterPlugin.message(sender, "invalid_material","AIR");
                return;
            }

            user.removeMaterial(material);
            ItemFilterPlugin.message(sender, "filter_remove", material.name());
        });


    }
    @Override
    public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
        List<String> result = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return result;
        }

        Optional<User> user = ItemFilterPlugin.getInstance().getUserManager().getUser(((Player) sender).getUniqueId());

        if(user.isPresent())
        {
            result.addAll(user.get().getMaterials().stream().map(Enum::name).collect(Collectors.toList()));
        }else{
            result.addAll(Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList()));
        }


        return result;
    }
}
