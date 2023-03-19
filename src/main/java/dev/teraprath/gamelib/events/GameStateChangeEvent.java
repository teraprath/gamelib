package dev.teraprath.gamelib.events;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class GameStateChangeEvent extends Event {

    private final GameState gameState;
    private final Game game;

    public GameStateChangeEvent(final GameState gameState, final Game game) {
        this.gameState = gameState;
        this.game = game;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return null;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Game getGame() { return this.game; }

}
