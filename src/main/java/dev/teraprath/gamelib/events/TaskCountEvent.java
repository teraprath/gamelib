package dev.teraprath.gamelib.events;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.task.CountdownTask;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TaskCountEvent extends Event implements Cancellable {

    private final CountdownTask task;
    private final Game game;
    private boolean cancelled;

    public TaskCountEvent(final CountdownTask task, final Game game) {
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
        this.task.setCancelled(true);
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public Game getGame() { return this.game; }

    public CountdownTask getTask() { return this.task; }

}
