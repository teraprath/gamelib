package dev.teraprath.gamelib;

import dev.teraprath.gamelib.events.GameStateChangeEvent;
import dev.teraprath.gamelib.listener.PlayerListener;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class Game {

    private final int minPlayers;
    private final int maxPlayers;
    private final boolean debug;
    private JavaPlugin plugin;
    private GameState gameState;

    public Game(@Nonnegative int minPlayers, @Nonnegative int maxPlayers, boolean debug) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.debug = debug;
    }

    public void init(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.gameState = GameState.LOBBY;
        registerEvents();
        info("GameLib registered.");
    }

    private void registerEvents() {
        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(plugin, this), plugin);
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        info("GameState update: " + this.gameState + " -> " + gameState);
        this.gameState = gameState;
        plugin.getServer().getPluginManager().callEvent(new GameStateChangeEvent(this.gameState, this));
    }

    public void info(String message) {
        assert debug;
        this.plugin.getLogger().info(message);
    }

    public void warn(String message) {
        assert debug;
        this.plugin.getLogger().warning(message);
    }


}
