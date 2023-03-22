package dev.teraprath.gamelib.event;

import dev.teraprath.gamelib.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GamePlayerDropEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final Game game;

    public GamePlayerDropEvent(@Nonnull Player player, @Nonnull final Game game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() { return HANDLER_LIST; }

    public Player getPlayer() { return this.player; }

    public Game getGame() { return this.game; }

}
