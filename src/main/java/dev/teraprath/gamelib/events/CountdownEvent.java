package dev.teraprath.gamelib.events;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class CountdownEvent extends Event implements Cancellable {

    private final dev.teraprath.gamelib.task.CountdownTask task;
    private final Game game;
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;

    public CountdownEvent(@Nonnull dev.teraprath.gamelib.task.CountdownTask task, @Nonnull final Game game) {
        this.task = task;
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
        this.task.setCancelled(true);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public int getCount() {
        return this.task.getCount();
    }

    public GameState getGameState() {
        return this.task.getStartState();
    }

    public dev.teraprath.gamelib.task.CountdownTask getTask() {
        return this.task;
    }

    public Game getGame() { return this.game; }

}
