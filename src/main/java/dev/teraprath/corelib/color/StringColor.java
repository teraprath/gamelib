package dev.teraprath.corelib.color;

import org.bukkit.ChatColor;

public class StringColor {

    public static String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
