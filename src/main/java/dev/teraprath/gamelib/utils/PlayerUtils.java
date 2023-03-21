package dev.teraprath.gamelib.utils;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.events.GamePlayerDropEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerUtils {

    private final Player player;
    private final Game game;

    public PlayerUtils(Player player, Game game) {
        this.player = player;
        this.game = game;
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
        if (game.getSpectatorSpawn() != null) { player.teleport(game.getSpectatorSpawn()); }
    }

}
