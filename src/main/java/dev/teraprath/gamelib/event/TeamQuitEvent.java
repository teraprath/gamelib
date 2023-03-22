package dev.teraprath.gamelib.event;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TeamQuitEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final Team team;
    private final Game game;

    public TeamQuitEvent(@Nonnull Player player, @Nonnull Team team, @Nonnull final Game game) {
        this.player = player;
        this.team = team;
        this.game = game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Player getPlayer() { return this.player; }

    public Team getTeam() {
        return this.team;
    }

    public Game getGame() { return this.game; }

}
