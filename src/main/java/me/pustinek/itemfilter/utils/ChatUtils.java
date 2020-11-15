package me.pustinek.itemfilter.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {


    public static String chatColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    static List<String> chatColor(List<String> lore) {
        return lore.stream().map(ChatUtils::chatColor).collect(Collectors.toList());
    }


}
