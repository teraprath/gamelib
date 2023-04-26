package dev.teraprath.gamelib.game;

import dev.teraprath.gamelib.event.GameJoinEvent;
import dev.teraprath.gamelib.event.GameQuitEvent;
import dev.teraprath.gamelib.event.GameSpectateEvent;
import dev.teraprath.gamelib.listener.GameDamageListener;
import dev.teraprath.gamelib.listener.GameJoinQuitListener;
import dev.teraprath.gamelib.listener.GameProtectListener;
import dev.teraprath.gamelib.listener.GameServerListener;
import dev.teraprath.gamelib.team.TeamManager;
import dev.teraprath.gamelib.timer.RunningTimer;
import dev.teraprath.gamelib.timer.ShutdownTimer;
import dev.teraprath.gamelib.timer.WaitingTimer;
import dev.teraprath.gamelib.utils.ServerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class Game {

    private final JavaPlugin plugin;
    private final GameSettings gameSettings;
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final Set<Player> players;
    private final Set<Player> spectators;
    private final WaitingTimer waitingTimer;
    private final RunningTimer runningTimer;
    private final ShutdownTimer shutdownTimer;
    private Location waitingSpawn;
    private Location spectatorSpawn;

    public Game(@Nonnull JavaPlugin plugin, @Nonnull GameSettings gameSettings) {
        this.plugin = plugin;
        this.gameSettings = gameSettings;
        this.gameStateManager = new GameStateManager(this.plugin, this);
        this.teamManager = new TeamManager();
        this.players = new HashSet<>();
        this.spectators = new HashSet<>();
        this.waitingTimer = new WaitingTimer(this.plugin, this, gameSettings.getWaitingTime());
        this.runningTimer = new RunningTimer(this.plugin, this, gameSettings.getRunningTime());
        this.shutdownTimer = new ShutdownTimer(this.plugin, this, gameSettings.getShutdownTime());
    }

    public Game init() {
        registerEvents();
        updateSlots(this.gameSettings.getMaxPlayers());
        return this;
    }

    private void registerEvents() {
        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new GameProtectListener(plugin, this), plugin);
        pm.registerEvents(new GameJoinQuitListener(plugin, this), plugin);
        pm.registerEvents(new GameDamageListener(this), plugin);
        pm.registerEvents(new GameServerListener(this), plugin);
    }

    private void updateSlots(int slots) {
        try {
            new ServerUtils(plugin, this).changeSlots(slots);
        } catch (ReflectiveOperationException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }


    public GameStateManager getGameStateManager() {
        return this.gameStateManager;
    }

    public void join(@Nonnull Player player) {

        if (!(this.players.contains(player))) {
            this.players.add(player);
            plugin.getServer().getPluginManager().callEvent(new GameJoinEvent(player, this));
            if (waitingSpawn != null) { player.teleport(waitingSpawn); }
        }

        if (gameStateManager.getGameState().equals(GameState.WAITING)) {

            if (this.players.size() >= gameSettings.getMinPlayers()) {
                if (!waitingTimer.isRunning()) {
                    waitingTimer.start();
                }
            }
        } else {
            addSpectator(player);
        }
    }

    public void quit(@Nonnull Player player) {
        if (this.players.contains(player)) {
            this.players.remove(player);
            this.spectators.remove(player);
            plugin.getServer().getPluginManager().callEvent(new GameQuitEvent(player, this));
        }
        if (this.players.size() < gameSettings.getMinPlayers()) {
            if (waitingTimer.isRunning()) {
                waitingTimer.stop();
            }
        }
    }

    public Set<Player> getOnlinePlayers() {
        return this.players;
    }

    public void stop(boolean shutdown) {
        for (Player player : this.players) {
            quit(player);
        }
        if (shutdown) {
            this.plugin.getServer().shutdown();
        }
    }

    public GameSettings getGameSettings() {
        return this.gameSettings;
    }

    public RunningTimer getRunningTimer() {
        return this.runningTimer;
    }

    public WaitingTimer getWaitingTimer() {
        return this.waitingTimer;
    }

    public ShutdownTimer getShutdownTimer() {
        return this.shutdownTimer;
    }

    public Location getWaitingSpawn() {
        return this.waitingSpawn;
    }

    public void setWaitingSpawn(@Nonnull Location location) {
        this.waitingSpawn = location;
    }

    public Location getSpectatorSpawn() {
        return this.spectatorSpawn;
    }

    public void setSpectatorSpawn(@Nonnull Location location) {
        this.spectatorSpawn = location;
    }

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public void addSpectator(@Nonnull Player player) {
        this.spectators.add(player);
        player.setGameMode(GameMode.SPECTATOR);
        if (spectatorSpawn != null) { player.teleport(waitingSpawn); }
        plugin.getServer().getPluginManager().callEvent(new GameSpectateEvent(player, this));
    }

    public Set<Player> getSpectators() {
        return this.spectators;
    }

    public Set<Player> getPlayersAlive() {
        Set<Player> alive = new HashSet<>();
        this.players.forEach(player -> {
            if (!this.spectators.contains(player)) {
                alive.add(player);
            }
        });
        return alive;
    }

}
