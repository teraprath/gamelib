package dev.teraprath.gamelib.team;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.events.TeamJoinEvent;
import dev.teraprath.gamelib.events.TeamQuitEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.UUID;

public class Team {

    private final JavaPlugin plugin;
    private final Game game;
    private final String name;
    private final int maxPlayers;
    private final UUID uuid;
    private ArrayList<Player> member;
    private String prefix;
    private ChatColor color;
    private int points;
    private boolean friendlyFire;
    private TeamInventory inventory;
    private Location spawnLocation;

    public Team(@Nonnull JavaPlugin plugin, @Nonnull Game game, @Nonnull String name, @Nonnegative int maxPlayers) {
        this.plugin = plugin;
        this.game = game;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.uuid = UUID.randomUUID();
        this.member = new ArrayList<>();
        this.points = 0;
        this.friendlyFire = true;
        this.inventory = new TeamInventory();
    }

    private void update() {
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            Scoreboard scoreboard = player.getScoreboard();
            plugin.getServer().getOnlinePlayers().forEach(all -> {
                if (game.getTeamByPlayer(all) != null) {
                    Team team = game.getTeamByPlayer(all);
                    if (scoreboard.getTeam(team.getName()) == null) { scoreboard.registerNewTeam(team.getName()); }
                    if (color != null) { scoreboard.getTeam(team.getName()).setColor(color); }
                    if (prefix != null) { scoreboard.getTeam(team.getName()).setPrefix(prefix); }
                }
            });
        });
    }

    public void setFriendlyFire(boolean enabled) {
        this.friendlyFire = enabled;
    }

    public boolean isFriendlyFire() {
        return this.friendlyFire;
    }

    public void setPrefix(@Nonnull String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setColor(@Nonnull ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public void addPoints(int amount) {
        this.points = this.points + amount;
    }

    public void removePoints(int amount) {
        this.points = this.points - amount;
    }

    public void setPoints(int amount) {
        this.points = amount;
    }

    public int getPoints() {
        return this.points;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void addMember(@Nonnull final Player player) {
        assert member.size() > maxPlayers;
        member.add(player);
        plugin.getServer().getPluginManager().callEvent(new TeamJoinEvent(player, this, this.game));
        game.info("Team update: " + this.getName() + " (" + this.getUniqueId() + ") -> ADDED: " + player.getName());
        update();
    }

    public void removeMember(@Nonnull final Player player) {
        member.remove(player);
        plugin.getServer().getPluginManager().callEvent(new TeamQuitEvent(player, this, this.game));
        game.info("Team update: " + this.getName() + " (" + this.getUniqueId() + ") -> REMOVED: " + player.getName());
        update();
    }

    public ArrayList<Player> getMember() {
        return this.member;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public TeamInventory getInventory() {
        return this.inventory;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public void giveInventory() {
        this.member.forEach(this::giveInventory);
    }

    public void giveInventory(@Nonnull final Player player) {
        this.getInventory().getContents().forEach((integer, itemStack) -> {
            player.getInventory().setItem(integer, itemStack);
        });
    }

}
