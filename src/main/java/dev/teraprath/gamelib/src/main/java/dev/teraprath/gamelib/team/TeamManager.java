package dev.teraprath.gamelib.team;

import dev.teraprath.gamelib.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeamManager {

    private final Map<String, Team> teamMap;


    public TeamManager() {
        this.teamMap = new HashMap<>();
    }

    public Set<Team> getTeams() {
        Set<Team> set = new HashSet<>();
        teamMap.forEach((s, team) -> {
            set.add(team);
        });
        return set;
    }

    public Team getTeam(@Nonnull String name) {
        return this.teamMap.get(name);
    }

    public void addTeam(@Nonnull Team team) {
        this.teamMap.put(team.getName(), team);
    }

    public void removeTeam(@Nonnull Team team) {
        this.teamMap.remove(team.getName());
    }

    public void removeTeam(@Nonnull String name) {
        this.teamMap.remove(name);
    }

}
