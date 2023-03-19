package dev.teraprath.corelib.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerInfo {

    final Player player;
    final Map<String, Object> data;

    public PlayerInfo(final Player player, final Map<String, Object> defaults) {
        this.player = player;
        this.data = new HashMap<>();
        this.data.putAll(defaults);
    }

    public Player getPlayer() {
        return this.player;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public Object getData(String key) {
        return this.data.get(key);
    }

    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

}
