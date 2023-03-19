package dev.teraprath.corelib.scoreboard;

import org.bukkit.ChatColor;

public enum ScoreboardEntry {

    ENTRY_0(0, ChatColor.RED.toString()),
    ENTRY_1(1, ChatColor.LIGHT_PURPLE.toString()),
    ENTRY_2(2, ChatColor.BLACK.toString()),
    ENTRY_3(3, ChatColor.BLUE.toString()),
    ENTRY_4(4, ChatColor.DARK_AQUA.toString()),
    ENTRY_5(5, ChatColor.DARK_BLUE.toString()),
    ENTRY_6(6, ChatColor.DARK_GREEN.toString()),
    ENTRY_7(7, ChatColor.YELLOW.toString()),
    ENTRY_8(8, ChatColor.BOLD.toString()),
    ENTRY_9(9, ChatColor.UNDERLINE.toString()),
    ENTRY_10(10, ChatColor.GREEN.toString()),
    ENTRY_11(11, ChatColor.WHITE.toString()),
    ENTRY_12(12, ChatColor.AQUA.toString()),
    ENTRY_13(13, ChatColor.DARK_PURPLE.toString()),
    ENTRY_14(14, ChatColor.DARK_RED.toString()),
    ENTRY_15(15, ChatColor.RESET.toString());

    private final int entry;
    private final String ScoreboardEntry;

    ScoreboardEntry(int entry, String ScoreboardEntry) {
        this.entry = entry;
        this.ScoreboardEntry = ScoreboardEntry;
    }

    public int getEntry() {
        return entry;
    }

    public String getScoreboardEntry() {
        return ScoreboardEntry;
    }
}


