package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class GameServerListener implements Listener {

    private final Game game;

    public GameServerListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        e.setMotd(game.getGameSettings().getMotd(game.getGameStateManager().getGameState()));
    }

}
