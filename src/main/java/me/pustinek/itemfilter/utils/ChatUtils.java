package me.pustinek.itemfilter.utils;


import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ChatUtils {
    final static Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})");
    final static Character COLOR_CHAR = '\u00A7';
    public static String chatColor(String s) {
        s = translateHexColorCodes(s);
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String translateHexColorCodes(String message)
    {
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    static List<String> chatColor(List<String> lore) {
        return lore.stream().map(ChatUtils::chatColor).collect(Collectors.toList());
    }


}
