package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GameChatListener implements Listener {

    private final Game game;

    public GameChatListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        final Team team = game.getTeamManager().parseTeam(player);
        if (team != null) {
            String prefix = team.getPrefix();
            ChatColor color = team.getColor();
            e.setFormat((prefix != null ? prefix : "") + (color != null ? color : ChatColor.GRAY) + player.getName() + " §8» §f" + "%2$s");
            return;
        }
        e.setFormat(ChatColor.GRAY + player.getName() + " §8» §f" + "%2$s");
    }

}
