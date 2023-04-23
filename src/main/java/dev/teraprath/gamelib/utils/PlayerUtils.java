package dev.teraprath.gamelib.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class PlayerUtils {

    public static void reset(@Nonnull Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.getInventory().clear();
    }

}
