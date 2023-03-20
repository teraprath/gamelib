package dev.teraprath.gamelib.manager;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration config;
    private final Map<String, Location> locations;

    public LocationManager(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder() + "/" + fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
        this.locations = new HashMap<>();
        reload();
    }

    private void reload() {
        if (file.exists()) {
            config.getConfigurationSection("locations").getKeys(false).forEach(s -> {
                Location location = config.getLocation("locations." + s);
                this.locations.put(s, location);
            });
        }
    }

    public Location getLocation(@Nonnull String name) {
        return this.locations.get(name);
    }

    public void addLocation(@Nonnull String name, @Nonnull Location location) {
        this.locations.put(name, location);
    }

    public void removeLocation(@Nonnull String name, @Nonnull Location location) {
        this.locations.put(name, location);
    }

    public Map<String, Location> getLocations() {
        return this.locations;
    }

    public void save() {
        try {
            this.locations.forEach((s, location) -> {
                config.set("locations." + s, location);
            });
            this.config.save(file);
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
