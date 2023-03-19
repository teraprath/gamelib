package dev.teraprath.gamelib;

import dev.teraprath.gamelib.listener.PlayerListener;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class Game {

    private final int minPlayers;
    private final int maxPlayers;
    private JavaPlugin plugin;
    private GameState gameState;

    public Game(@Nonnegative int minPlayers, @Nonnegative int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public Game init(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.gameState = GameState.LOBBY;
        registerEvents();
        return this;
    }

    private void registerEvents() {
        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), plugin);
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }



}
