package dev.teraprath.gamelib;

import dev.teraprath.gamelib.event.GameOverEvent;
import dev.teraprath.gamelib.event.GamePlayerDropEvent;
import dev.teraprath.gamelib.event.LobbyQuitEvent;
import dev.teraprath.gamelib.event.GameStateChangeEvent;
import dev.teraprath.gamelib.listener.*;
import dev.teraprath.gamelib.manager.ConfigurationManager;
import dev.teraprath.gamelib.manager.LocationManager;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.task.CountdownTask;
import dev.teraprath.gamelib.team.Team;
import dev.teraprath.gamelib.team.TeamManager;
import dev.teraprath.gamelib.utils.PlayerUtils;
import dev.teraprath.gamelib.utils.ServerUtils;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Game {

    private JavaPlugin plugin;
    private final int minPlayers;
    private final int maxPlayers;
    private ConfigurationManager configuration;
    private LocationManager locationManager;
    private TeamManager teamManager;
    private int lobbyCountdown;
    private int shutdownCountdown;
    private int gameLength;
    private GameState gameState;
    private ArrayList<Player> players;
    private boolean waiting;
    private Location lobbySpawn;
    private Location spectatorSpawn;

    private boolean VIPJoin;
    private String VIPJoinPermission;
    private String VIPJoinFullMessage;
    private String VIPJoinKickMessage;
    private Map<GameState, String> motd;
    private boolean teamRefill;
    private String shutdownMessage;

    public Game(@Nonnegative int minPlayers, @Nonnegative int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyCountdown = 60;
        this.shutdownCountdown = 20;
        this.gameLength = 300;
        this.players = new ArrayList<>();
        this.waiting = true;
        this.VIPJoin = false;
        this.teamRefill = false;
        this.motd = new HashMap<>();
    }

    public Game init(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.gameState = GameState.LOBBY;
        this.teamManager = new TeamManager(this.plugin, this);
        this.configuration = new ConfigurationManager(plugin, "config.yml");
        this.locationManager = new LocationManager(plugin, "locations.yml");
        updateSlots(this.maxPlayers);
        registerEvents();
        updateWorlds();
        printStats();
        return this;
    }

    private void updateSlots(int slots) {
        try {
            new ServerUtils(plugin, this).changeSlots(slots);
        } catch (ReflectiveOperationException e) {
            error(e.getMessage());
        }
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

    public Game setMotd(GameState gameState, String motd) {
        this.motd.put(gameState, motd);
        return this;
    }

    public String getMotd(GameState gameState) {
        final String motd = this.motd.get(gameState);
        return motd != null ? motd : gameState.name();
    }

    public Game enableVIPJoin(@Nonnull String permission, @Nonnull String fullMessage, @Nonnull String kickMessage) {
        this.VIPJoin = true;
        this.VIPJoinPermission = permission;
        this.VIPJoinFullMessage = fullMessage;
        this.VIPJoinKickMessage = kickMessage;
        return this;
    }

    public Game enableTeamRefill() {
        this.teamRefill = true;
        return this;
    }

    public boolean hasTeamRefill() {
        return this.teamRefill;
    }

    public boolean hasVIPJoin() {
        return this.VIPJoin;
    }

    public String getVIPJoinPermission() {
        return this.VIPJoinPermission;
    }

    public String getVIPJoinFullMessage() {
        return this.VIPJoinFullMessage;
    }

    public String getVIPJoinKickMessage() {
        return this.VIPJoinKickMessage;
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

    public Game setShutdownMessage(String message) {
        this.shutdownMessage = shutdownMessage;
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
        pm.registerEvents(new GameProtectListener(plugin, this), plugin);
        pm.registerEvents(new GameChatListener(this), plugin);
        pm.registerEvents(new GameDamageListener(this), plugin);
        pm.registerEvents(new GameJoinQuitListener(plugin, this), plugin);
        pm.registerEvents(new GameServerListener(this), plugin);
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
                handleTeams();
                new CountdownTask(plugin, this, GameState.GAME, GameState.END, this.getGameLength()).start();
            }
            case END -> new CountdownTask(plugin, this, GameState.END, GameState.SHUTDOWN, this.getShutdownCountdown()).start();
            case SHUTDOWN -> plugin.getServer().shutdown();
        }
    }

    private void handleTeams() {

        // Team refill
        if (this.hasTeamRefill()) {
            this.getPlayers().forEach(player -> {
                if (this.getTeamManager().parseTeam(player) == null) {
                    this.getTeamManager().getTeams().forEach(team -> {
                        team.addMember(player);
                    });
                }
            });
        }

        // Team teleport
        for (Team team : teamManager.getTeams()) {
            if (team.getSpawnLocation() != null) {
                team.getMember().forEach(player -> {
                    player.setBedSpawnLocation(team.getSpawnLocation());
                    player.teleport(team.getSpawnLocation());
                });
            }
        }

    }

    public void info(String message) {
        if (configuration.getDebugLevel() > 2) { this.plugin.getLogger().info(message); }
    }

    public void warning(String message) {
        if (configuration.getDebugLevel() > 1) { this.plugin.getLogger().warning(message); }
    }

    public void error(String message) {
        if (configuration.getDebugLevel() > 1) { plugin.getLogger().log(Level.SEVERE, message); }
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

    public void drop(@Nonnull Player player) {

        this.players.remove(player);
        new PlayerUtils(player, this).toSpectator();

        if (gameState.equals(GameState.LOBBY)) {
            plugin.getServer().getPluginManager().callEvent(new LobbyQuitEvent(player, this));
        }

        plugin.getServer().getPluginManager().callEvent(new GamePlayerDropEvent(player, this));

    }

    public void over(ArrayList<Player> winner) {
        if (winner != null) {
            players.forEach(player -> {
                if (!winner.contains(player)) {
                    drop(player);
                }
            });
        }
        plugin.getServer().getPluginManager().callEvent(new GameOverEvent(winner, this));
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

    public void setLobbySpawn(Location location) {
        this.lobbySpawn = location;
    }

    public Location getLobbySpawn() {
        return this.lobbySpawn;
    }

    public void setSpectatorSpawn(Location location) {
        this.spectatorSpawn = location;
    }

    public Location getSpectatorSpawn() {
        return this.spectatorSpawn;
    }

    public ConfigurationManager getConfiguration() {
        return this.configuration;
    }

    public LocationManager getLocationManager() {
        return this.locationManager;
    }

}
