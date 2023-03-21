package dev.teraprath.gamelib;

import dev.teraprath.gamelib.events.GameJoinEvent;
import dev.teraprath.gamelib.events.GameQuitEvent;
import dev.teraprath.gamelib.events.GameStateChangeEvent;
import dev.teraprath.gamelib.listener.PlayerListener;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.task.CountdownTask;
import dev.teraprath.gamelib.team.Team;
import dev.teraprath.gamelib.team.TeamManager;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;

public class Game {

    private JavaPlugin plugin;
    private final int minPlayers;
    private final int maxPlayers;
    private final boolean debug;
    private TeamManager teamManager;
    private int lobbyCountdown;
    private int shutdownCountdown;
    private int gameLength;
    private GameState gameState;
    private ArrayList<Player> players;
    private boolean waiting;

    public Game(@Nonnegative int minPlayers, @Nonnegative int maxPlayers,  boolean debug) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyCountdown = 60;
        this.shutdownCountdown = 20;
        this.gameLength = 300;
        this.debug = debug;
        this.players = new ArrayList<>();
        this.waiting = true;
    }

    public Game init(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.gameState = GameState.LOBBY;
        this.teamManager = new TeamManager(this.plugin, this);
        registerEvents();
        updateWorlds();
        printStats();
        return this;
    }

    private void updateWorlds() {
        plugin.getServer().getWorlds().forEach(world -> {
            world.setStorm(false);
            world.setThundering(false);
            world.setTime(1000);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            world.setGameRule(GameRule.SPAWN_RADIUS, 0);
        });
    }

    private void printStats() {
        plugin.getLogger().info("GameLib registered.");
        info("gameLength: " + gameLength);
        info("lobbyCountdown: " + lobbyCountdown);
        info("shutdownCountdown: " + shutdownCountdown);
        info("minPlayers: " + minPlayers);
        info("maxPlayers: " + maxPlayers);
    }

    public Game setGameLength(@Nonnegative int seconds) {
        this.gameLength = seconds;
        return this;
    }

    public Game setLobbyCountdown(@Nonnegative int seconds) {
        this.lobbyCountdown = seconds;
        return this;
    }

    public Game setShutdownCountdown(@Nonnegative int seconds) {
        this.shutdownCountdown = seconds;
        return this;
    }

    public void setWaiting(boolean enabled) {
        this.waiting = enabled;
    }

    public boolean isWaiting() {
        return this.waiting;
    }

    private void registerEvents() {
        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(plugin, this), plugin);
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(@Nonnull GameState gameState) {
        info("GameState update: " + this.gameState + " -> " + gameState);
        this.gameState = gameState;
        listenState(gameState);
        plugin.getServer().getPluginManager().callEvent(new GameStateChangeEvent(this.gameState, this));
    }

    private void listenState(GameState gameState) {
        switch (gameState) {
            case GAME -> {
                for (Team team : teamManager.getTeams()) {
                    team.equip();
                    if (team.getSpawnLocation() != null) {
                        team.getMember().forEach(player -> {
                            player.setBedSpawnLocation(team.getSpawnLocation());
                            player.teleport(team.getSpawnLocation());
                        });
                    }
                }
                new CountdownTask(plugin, this, GameState.GAME, GameState.END, this.getGameLength()).start();
            }
            case END -> new CountdownTask(plugin, this, GameState.END, GameState.SHUTDOWN, this.getShutdownCountdown()).start();
            case SHUTDOWN -> plugin.getServer().shutdown();
        }
    }

    public void info(String message) {
        if (debug) { this.plugin.getLogger().info(message); }
    }

    public void warning(String message) {
        if (debug) { this.plugin.getLogger().warning(message); }
    }

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void join(@Nonnull Player player) {
        this.players.add(player);
        plugin.getServer().getPluginManager().callEvent(new GameJoinEvent(player, this));
    }

    public void quit(@Nonnull Player player) {
        this.players.remove(player);
        plugin.getServer().getPluginManager().callEvent(new GameQuitEvent(player, this));
        getTeamManager().getTeams().forEach(team -> {
            team.removeMember(player);
        });
    }

    public int getLobbyCountdown() {
        return this.lobbyCountdown;
    }

    public int getShutdownCountdown() {
        return this.shutdownCountdown;
    }

    public int getGameLength() {
        return this.gameLength;
    }

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

}
