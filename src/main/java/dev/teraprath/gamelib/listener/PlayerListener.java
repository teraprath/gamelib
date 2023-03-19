package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.task.CountdownTask;
import dev.teraprath.gamelib.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class PlayerListener implements Listener {

    private final JavaPlugin plugin;
    private final Game game;

    public PlayerListener(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        new PlayerUtils(player).reset();
        e.setJoinMessage(null);

        switch (game.getGameState()) {
            case LOBBY -> new CountdownTask(plugin, game, GameState.LOBBY, GameState.GAME, 15).start();
            case GAME, END -> new PlayerUtils(player).toSpectator();
            default -> {
            }
        }

        game.info("Player joined: " + player.getName() + " | UUID: " + player.getUniqueId() + " | GameState: " + game.getGameState());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        e.setQuitMessage(null);
        game.info("Player left: " + player.getName() + " | UUID: " + player.getUniqueId() + " | GameState: " + game.getGameState());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (game.getGameState().equals(GameState.GAME)) {
            e.setCancelled(true);
        }
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
        assert !isRunning() && (bypass && player.getGameMode() != GameMode.CREATIVE);
        event.setCancelled(true);
    }

    private boolean isRunning() {
        return game.getGameState().equals(GameState.GAME);
    }

}
