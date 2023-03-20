package dev.teraprath.gamelib;

import dev.teraprath.gamelib.events.GameStateChangeEvent;
import dev.teraprath.gamelib.listener.PlayerListener;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.team.Team;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Game {

    private final int minPlayers;
    private final int maxPlayers;
    private final boolean debug;
    private JavaPlugin plugin;
    private GameState gameState;
    private Map<UUID, Team> teams;
    private ArrayList<Player> players;
    private int lobbyCountdown;

    public Game(@Nonnegative int minPlayers, @Nonnegative int maxPlayers, boolean debug) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.debug = debug;
        this.teams = new HashMap<>();
        this.players = new ArrayList<>();
        this.lobbyCountdown = 60;
    }

    public void init(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.gameState = GameState.LOBBY;
        registerEvents();
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
        info("GameLib registered.");
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

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public ArrayList<Team> getTeams() {
        ArrayList<Team> list = new ArrayList<>();
        this.teams.forEach((uuid, team) -> {
            list.add(team);
        });
        return list;
    }

    public Team getTeam(@Nonnull String name) {
        for (Team team : this.teams.values()) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeam(@Nonnull UUID uuid) {
        return this.teams.get(uuid);
    }

    public Team createTeam(@Nonnull String name, @Nonnegative int maxPlayers) {
        final Team team = new Team(plugin, this, name, maxPlayers);
        this.teams.put(team.getUniqueId(), team);
        info("New Team created: " + team.getName() + " -> " + team.getUniqueId());
        return team;
    }

    public void deleteTeam(@Nonnull String name) {
        deleteTeam(getTeam(name));
    }

    public void deleteTeam(@Nonnull UUID uuid) {
        deleteTeam(getTeam(uuid));
    }

    public void deleteTeam(@Nonnull Team team) {
        this.teams.remove(team.getUniqueId());
        info("Team removed: " + team.getName() + " -> " + team.getUniqueId());
    }

    public Team getTeamByPlayer(@Nonnull String name) {
        final Player player = plugin.getServer().getPlayer(name);
        if (player == null) { return null; }
        return getTeamByPlayer(player);
    }

    public Team getTeamByPlayer(@Nonnull UUID uuid) {
        final Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) { return null; }
        return getTeamByPlayer(player);
    }

    public Team getTeamByPlayer(@Nonnull final Player player) {
        for (Team team : this.teams.values()) {
            if (team.getMember().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public int getLobbyCountdown() {
        return this.lobbyCountdown;
    }

    public void setLobbyCountdown(int seconds) {
        this.lobbyCountdown = seconds;
    }

}
