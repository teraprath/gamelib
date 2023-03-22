package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.event.LobbyJoinEvent;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.task.CountdownTask;
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
    public void onPlayerLogin(PlayerLoginEvent e) {

        final Player player = e.getPlayer();
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {

            if (game.getGameState().equals(GameState.GAME)) {
                e.allow();
                return;
            }

            if (game.hasVIPJoin()) {
                if (player.hasPermission(game.getVIPJoinPermission())) {
                    for (Player target : game.getPlayers()) {
                        if (!(target.hasPermission(game.getVIPJoinPermission()))) {
                            target.kickPlayer(game.getVIPJoinKickMessage());
                            e.allow();
                            return;
                        }
                    }
                }
                e.setKickMessage(game.getVIPJoinFullMessage());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();

        e.setJoinMessage(null);
        new PlayerUtils(player, game).reset();

        // Check game state
        switch (game.getGameState()) {
            case LOBBY -> handleLobbyJoin(player);
            case GAME, END -> new PlayerUtils(player, game).toSpectator();
            default -> {
            }
        }

    }

    private void handleLobbyJoin(final Player player) {

        if (game.getLobbySpawn() != null) { player.teleport(game.getLobbySpawn()); }

        if (game.getPlayers().size() < game.getMaxPlayers()) {
            game.getPlayers().add(player);
            plugin.getServer().getPluginManager().callEvent(new LobbyJoinEvent(player, game));
        }

        assert game.isWaiting();

        if (game.getPlayers().size() >= game.getMinPlayers()) {
            game.setWaiting(false);
            new CountdownTask(plugin, game, GameState.LOBBY, GameState.GAME, game.getLobbyCountdown()).start();
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        final Player player = e.getPlayer();
        e.setQuitMessage(null);

        game.getTeamManager().getTeams().forEach(team -> {
            team.removeMember(player);
        });

        game.drop(player);

    }

}
