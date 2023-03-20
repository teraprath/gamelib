package dev.teraprath.gamelib.events;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GameStateChangeEvent extends Event {

    private final GameState gameState;
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Game game;

    public GameStateChangeEvent(@Nonnull final GameState gameState, @Nonnull final Game game) {
        this.gameState = gameState;
        this.game = game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Game getGame() { return this.game; }

}
