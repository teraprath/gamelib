package dev.teraprath.gamelib.task;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class CountdownTask {

    private final JavaPlugin plugin;
    private final Game game;
    private int countdowm;
    private final GameState startState;
    private final GameState endState;

    public CountdownTask(@Nonnull JavaPlugin plugin, @Nonnull Game game, @Nonnull GameState startState, @Nonnull GameState endState, @Nonnegative int countdown) {
        this.game = game;
        this.plugin = plugin;
        this.countdowm = countdown;
        this.startState = startState;
        this.endState = endState;
    }

    public CountdownTask start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task -> {
            while (game.getGameState().equals(startState)) {
                if (countdowm == 0) {
                    game.setGameState(endState);
                }
                countdowm--;
            }
        }, 20, 20);
        return this;
    }

}
