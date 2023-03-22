package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class GameDamageListener implements Listener {

    private final Game game;

    public GameDamageListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)) { return; }

        // Check if game is running
        if (game.getGameState().equals(GameState.LOBBY) || game.getGameState().equals(GameState.END)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player)) { return; }

        // Check if game is running
        if (game.getGameState().equals(GameState.LOBBY) || game.getGameState().equals(GameState.END)) {
            e.setCancelled(true);
        }

        // Check if attacker is a player
        if (e.getDamager() instanceof Player) {

            final Player victim = (Player) e.getEntity();

            // Check friendly fire
            if (game.getTeamManager().parseTeam(victim) != null) {
                final Player attacker = (Player) e.getDamager();
                final Team victimTeam = game.getTeamManager().parseTeam(victim);
                final Team attackerTeam = game.getTeamManager().parseTeam(attacker);
                assert victimTeam != null && attackerTeam != null;
                if (victimTeam.equals(attackerTeam)) {
                    if (game.getTeamManager().isFriendlyFire()) {
                        e.setCancelled(true);
                    }
                }
            }

        }
    }

}
