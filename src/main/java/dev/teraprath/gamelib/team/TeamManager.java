package dev.teraprath.gamelib.team;

import dev.teraprath.gamelib.Game;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManager {

    private final JavaPlugin plugin;
    private final Game game;
    private final Map<UUID, Team> teams;

    public TeamManager(@Nonnull JavaPlugin plugin, @Nonnull Game game) {
        this.plugin = plugin;
        this.game = game;
        this.teams = new HashMap<>();
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
        final Team team = new Team(plugin, game, name, maxPlayers);
        this.teams.put(team.getUniqueId(), team);
        game.info("New Team created: " + team.getName() + " -> " + team.getUniqueId());
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
        game.info("Team removed: " + team.getName() + " -> " + team.getUniqueId());
    }

    public Team parseTeam(@Nonnull String name) {
        final Player player = plugin.getServer().getPlayer(name);
        if (player == null) { return null; }
        return parseTeam(player);
    }

    public Team parseTeam(@Nonnull UUID uuid) {
        final Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) { return null; }
        return parseTeam(player);
    }

    public Team parseTeam(@Nonnull final Player player) {
        for (Team team : this.teams.values()) {
            if (team.getMember().contains(player)) {
                return team;
            }
        }
        return null;
    }

}
