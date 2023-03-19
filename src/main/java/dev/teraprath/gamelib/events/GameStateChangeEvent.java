package dev.teraprath.gamelib.events;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event implements Cancellable {

    private final GameState gameState;
    private final Game game;
    private boolean cancelled;

    public GameStateChangeEvent(final GameState gameState, final Game game) {
        this.gameState = gameState;
        this.game = game;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Game getGame() { return this.game; }

}
