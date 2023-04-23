package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;

public class GameDamageListener implements Listener {

    private final Game game;

    public GameDamageListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)) { return; }

        // Check if game is running
        if (game.getGameStateManager().getGameState().equals(GameState.WAITING) || game.getGameStateManager().getGameState().equals(GameState.END)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player)) { return; }

        // Check if game is running
        if (game.getGameStateManager().getGameState().equals(GameState.WAITING) || game.getGameStateManager().getGameState().equals(GameState.END)) {
            e.setCancelled(true);
        }
    }

}
