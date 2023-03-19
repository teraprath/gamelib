package dev.teraprath.corelib.player;


import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PlayerInfo> players;
    private final Map<String, Object> defaults;

    public PlayerManager() {
        this.players = new HashMap<>();
        this.defaults = new HashMap<>();
    }

    public PlayerManager addDefault(String key, Object value) {
        this.defaults.put(key, value);
        return this;
    }

    public void createPlayer(@Nonnull Player player) {
        this.players.put(player.getUniqueId(), new PlayerInfo(player, defaults));
    }

    public void deletePlayer(@Nonnull Player player) {
        this.players.remove(player.getUniqueId());
    }

    public Map<UUID, PlayerInfo> getOnlinePlayers() {
        return this.players;
    }

    public PlayerInfo getPlayer(@Nonnull UUID uuid) {
        return this.players.get(uuid);
    }

    public Map<String, Object> getDefaults() {
        return this.defaults;
    }

}
