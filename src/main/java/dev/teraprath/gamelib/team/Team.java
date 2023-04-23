package dev.teraprath.gamelib.team;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class Team {

    private final String name;
    private Set<Player> members;

    public Team(String name) {
        this.name = name;
        this.members = new HashSet<>();
    }

    public String getName() {
        return this.name;
    }

    public Set<Player> getMembers() {
        return members;
    }

    public void addMember(@Nonnull Player player) {
        this.members.add(player);
    }

    public void removeMember(@Nonnull Player player) {
        this.members.remove(player);
    }

}
