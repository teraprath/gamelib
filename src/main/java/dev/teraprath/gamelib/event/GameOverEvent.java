package dev.teraprath.gamelib.event;

import dev.teraprath.gamelib.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class GameOverEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final ArrayList<Player> winner;
    private final Game game;

    public GameOverEvent(ArrayList<Player> winner, @Nonnull final Game game) {
        this.winner = winner;
        this.game = game;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() { return HANDLER_LIST; }

    public ArrayList<Player> getWinner() { return this.winner; }

    public Game getGame() { return this.game; }


}
