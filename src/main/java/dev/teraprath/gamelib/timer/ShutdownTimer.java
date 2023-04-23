package dev.teraprath.gamelib.timer;

import dev.teraprath.gamelib.event.RunningTimerEndEvent;
import dev.teraprath.gamelib.event.ShutdownTimerEndEvent;
import dev.teraprath.gamelib.event.ShutdownTimerRunEvent;
import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.game.GameState;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class ShutdownTimer extends Timer {

    private final Game game;

    public ShutdownTimer(@NotNull JavaPlugin plugin, @Nonnull Game game, @Nonnegative int length) {
        super(plugin, length);
        this.game = game;
    }

    @Override
    public void run() {

        if (game.getGameStateManager().getGameState().equals(GameState.END)) {
            plugin.getServer().getScheduler().runTask(plugin, sync -> {
                plugin.getServer().getPluginManager().callEvent(new ShutdownTimerRunEvent(this, this.game));
            });
        } else {
            this.stop();
        }

    }

    @Override
    public void end() {
        plugin.getServer().getScheduler().runTask(plugin, sync -> {
            plugin.getServer().getPluginManager().callEvent(new ShutdownTimerEndEvent(this, this.game));
        });
        plugin.getServer().shutdown();
    }

}
