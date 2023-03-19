package dev.teraprath.gamelib.task;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.events.GameStateChangeEvent;
import dev.teraprath.gamelib.events.TaskCountEvent;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.UUID;

public class CountdownTask {

    private final JavaPlugin plugin;
    private final Game game;
    private int countdown;
    private final GameState startState;
    private final GameState endState;
    private final UUID uuid;
    private boolean cancelled;

    public CountdownTask(@Nonnull JavaPlugin plugin, @Nonnull Game game, @Nonnull GameState startState, @Nonnull GameState endState, @Nonnegative int countdown) {
        this.game = game;
        this.plugin = plugin;
        this.countdown = countdown;
        this.startState = startState;
        this.endState = endState;
        this.uuid = UUID.randomUUID();
        this.cancelled = false;
    }

    public void start() {
        game.info(String.format("New Task started: %s, %s, %d seconds", startState, endState, countdown));
        game.info(String.format("UUID: %s", uuid));
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, task -> {
            while (game.getGameState().equals(startState) && !cancelled) {
                game.info(String.format("Task (%s) : %d seconds", uuid, countdown));
                if (countdown == 0) {
                    plugin.getServer().getScheduler().runTask(plugin, sync -> {
                        game.setGameState(endState);
                    });
                }
                countdown--;
            }
        }, 20, 20);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCount(int count) {
        this.countdown = count;
    }

    public int getCount() {
        return this.countdown;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public GameState getStartState() {
        return this.startState;
    }

    public GameState getEndState() {
        return this.endState;
    }

}
