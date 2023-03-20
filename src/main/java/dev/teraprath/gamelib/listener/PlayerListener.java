package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.task.CountdownTask;
import dev.teraprath.gamelib.team.Team;
import dev.teraprath.gamelib.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class PlayerListener implements Listener {

    private final JavaPlugin plugin;
    private final Game game;

    public PlayerListener(@Nonnull JavaPlugin plugin, @Nonnull Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();
        e.setJoinMessage(null);
        new PlayerUtils(player).reset();

        // Check game state
        switch (game.getGameState()) {
            case LOBBY -> checkMinPlayers(player);
            case GAME, END -> new PlayerUtils(player).toSpectator();
            default -> {
            }
        }

    }

    private void checkMinPlayers(final Player player) {

        if (game.getPlayers().size() < game.getMaxPlayers()) {
            game.join(player);
        }

        assert game.isWaiting();

        if (game.getPlayers().size() >= game.getMinPlayers()) {
            game.setWaiting(false);
            new CountdownTask(plugin, game, GameState.LOBBY, GameState.GAME, game.getLobbyCountdown()).start();
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        final Player player = e.getPlayer();
        e.setQuitMessage(null);
        game.quit(player);

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {

        assert e.getEntity() instanceof Player;

        // Check if game is running
        if (game.getGameState().equals(GameState.GAME) || game.getGameState().equals(GameState.END)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        assert e.getEntity() instanceof Player;

        // Check if game is running
        if (game.getGameState().equals(GameState.GAME) || game.getGameState().equals(GameState.END)) {
            e.setCancelled(true);
        }

        // Check if attacker is a player
        if (e.getDamager() instanceof Player) {

            final Player victim = (Player) e.getEntity();

            // Check friendly fire
            if (game.getTeamByPlayer(victim) != null) {
                final Player attacker = (Player) e.getDamager();
                final Team victimTeam = game.getTeamByPlayer(victim);
                final Team attackerTeam = game.getTeamByPlayer(attacker);
                assert victimTeam != null && attackerTeam != null;
                if (victimTeam.equals(attackerTeam)) {
                    if (victimTeam.isFriendlyFire()) {
                        e.setCancelled(true);
                    }
                }
            }

        }

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        final Team team = game.getTeamByPlayer(player);
        if (team != null) {
            String prefix = team.getPrefix();
            ChatColor color = team.getColor();
            e.setFormat((prefix != null ? prefix : "") + (color != null ? color : ChatColor.GRAY) + player.getName() + " §8» §f" + "%2$s");
            return;
        }
        e.setFormat(ChatColor.GRAY + player.getName() + " §8» §f" + "%2$s");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        protectEvent(e, true, e.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        protectEvent(e, true, e.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        protectEvent(e, true, e.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        protectEvent(e, true, (Player) e.getWhoClicked());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        protectEvent(e, true, e.getPlayer());
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        protectEvent(e, false, null);
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        protectEvent(e, true, e.getPlayer());
    }

    private void protectEvent(@Nonnull Cancellable event, boolean bypass, Player player) {
        if (game.getGameState() != GameState.GAME) {
            if (bypass && player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

}
