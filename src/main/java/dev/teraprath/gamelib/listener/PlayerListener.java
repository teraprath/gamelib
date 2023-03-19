package dev.teraprath.gamelib.listener;

import dev.teraprath.gamelib.Game;
import dev.teraprath.gamelib.state.GameState;
import dev.teraprath.gamelib.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Game game;

    public PlayerListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        new PlayerUtils(player).reset();
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        protectEvent(e);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        protectEvent(e);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        protectEvent(e);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        protectEvent(e);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        protectEvent(e);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        protectEvent(e);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        protectEvent(e);
    }

    private void protectEvent(Cancellable event) {
        assert !isRunning();
        event.setCancelled(true);
    }

    private boolean isRunning() {
        return game.getGameState().equals(GameState.GAME);
    }

}
