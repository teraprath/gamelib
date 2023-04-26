package dev.teraprath.gamelib.event;

import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.game.GameState;
import dev.teraprath.gamelib.game.GameStateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GameStateChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Game game;

    public GameStateChangeEvent(@Nonnull final Game game) {
        this.game = game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() { return HANDLER_LIST; }

    public GameState getGameState() {
        return this.game.getGameStateManager().getGameState();
    }

    public Game getGame() { return this.game; }

}
