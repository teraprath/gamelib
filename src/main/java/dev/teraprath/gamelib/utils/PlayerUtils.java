package dev.teraprath.gamelib.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerUtils {

    private final Player player;

    public PlayerUtils(Player player) {
        this.player = player;
    }

    public void reset() {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setExp(0);
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setHealth(20);
        player.setFoodLevel(20);
    }

    public void toSpectator() {
        reset();
        player.setGameMode(GameMode.SPECTATOR);
        player.setAllowFlight(true);
        player.setFlying(true);
    }

}
