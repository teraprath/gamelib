package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.game.Game;
import dev.teraprath.gamelib.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class GameProtectListener implements Listener {

    private final JavaPlugin plugin;
    private final Game game;

    public GameProtectListener(@Nonnull JavaPlugin plugin, @Nonnull Game game) {
        this.plugin = plugin;
        this.game = game;
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
        if (game.getGameStateManager().getGameState() != GameState.RUNNING) {
            if (bypass && player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

}
