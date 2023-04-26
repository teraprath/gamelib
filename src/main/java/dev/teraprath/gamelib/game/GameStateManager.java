package dev.teraprath.gamelib.game;

import dev.teraprath.gamelib.event.GameStateChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class GameStateManager {

    private final JavaPlugin plugin;
    private final Game game;
    private GameState gameState;

    public GameStateManager(@Nonnull JavaPlugin plugin, @Nonnull Game game) {
        this.plugin = plugin;
        this.game = game;
        this.gameState = GameState.WAITING;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(@Nonnull GameState gameState) {
        this.gameState = gameState;
        plugin.getServer().getPluginManager().callEvent(new GameStateChangeEvent(this.game));
    }

}
