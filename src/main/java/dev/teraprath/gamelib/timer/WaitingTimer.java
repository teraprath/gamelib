package dev.teraprath.gamelib.timer;

import dev.teraprath.gamelib.event.WaitingTimerEndEvent;
import dev.teraprath.gamelib.event.WaitingTimerRunEvent;
import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.game.GameState;
import dev.teraprath.gamelib.utils.PlayerUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class WaitingTimer extends Timer {

    private final Game game;

    public WaitingTimer(@NotNull JavaPlugin plugin, @Nonnull Game game, @Nonnegative int length) {
        super(plugin, length);
        this.game = game;
    }

    @Override
    public void run() {

        if (game.getGameStateManager().getGameState().equals(GameState.WAITING)) {
            plugin.getServer().getScheduler().runTask(plugin, sync -> {
                plugin.getServer().getPluginManager().callEvent(new WaitingTimerRunEvent(this, this.game));
            });
        } else {
            this.stop();
        }

    }

    @Override
    public void end() {
        this.game.getGameStateManager().setGameState(GameState.RUNNING);
        game.getRunningTimer().start();
        plugin.getServer().getScheduler().runTask(plugin, sync -> {
            plugin.getServer().getPluginManager().callEvent(new WaitingTimerEndEvent(this, this.game));
            game.getOnlinePlayers().forEach(PlayerUtils::reset);
        });

    }

}
