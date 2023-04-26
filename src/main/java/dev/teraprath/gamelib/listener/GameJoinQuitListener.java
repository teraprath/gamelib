package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.game.GameState;
import dev.teraprath.gamelib.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameJoinQuitListener implements Listener {

    private final JavaPlugin plugin;
    private final Game game;

    public GameJoinQuitListener(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();

        e.setJoinMessage(null);
        if (game.getGameStateManager().getGameState().equals(GameState.WAITING)) {
            game.join(player);
            PlayerUtils.reset(player);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        final Player player = e.getPlayer();
        e.setQuitMessage(null);
        game.quit(player);

    }

}