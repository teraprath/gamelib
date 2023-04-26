package dev.teraprath.gamelib.timer;

import dev.teraprath.gamelib.event.RunningTimerEvent;
import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.game.GameState;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class RunningTimer extends Timer {

    private final Game game;

    public RunningTimer(@NotNull JavaPlugin plugin, @Nonnull Game game, @Nonnegative int length) {
        super(plugin, length);
        this.game = game;
    }

    @Override
    public void run() {

        if (game.getGameStateManager().getGameState().equals(GameState.RUNNING)) {
            plugin.getServer().getScheduler().runTask(plugin, sync -> {
                plugin.getServer().getPluginManager().callEvent(new RunningTimerEvent(this, this.game));
            });
        } else {
            this.stop();
        }

    }

    @Override
    public void end() {
        this.game.getGameStateManager().setGameState(GameState.END);
        game.getShutdownTimer().start();
    }

}
