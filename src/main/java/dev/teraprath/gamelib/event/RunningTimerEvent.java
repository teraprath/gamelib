package dev.teraprath.gamelib.event;

import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.timer.RunningTimer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class RunningTimerEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final RunningTimer timer;
    private final Game game;

    public RunningTimerEvent(@Nonnull RunningTimer timer, @Nonnull final Game game) {
        this.timer = timer;
        this.game = game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() { return HANDLER_LIST; }

    public RunningTimer getTimer() {
        return this.timer;
    }

    public Game getGame() { return this.game; }

}
