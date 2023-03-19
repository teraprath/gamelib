package dev.teraprath.gamelib;

import dev.teraprath.gamelib.listener.PlayerListener;
import dev.teraprath.gamelib.state.GameState;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class GameLib {

    private final int minPlayers;
    private final int maxPlayers;
    private JavaPlugin plugin;
    private GameState gameState;

    public GameLib(@Nonnegative int minPlayers, @Nonnegative int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public GameLib init(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.gameState = GameState.LOBBY;
        registerEvents();
        return this;
    }

    private void registerEvents() {
        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), plugin);
    }



}
