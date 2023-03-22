package dev.teraprath.gamelib.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration config;
    private int debugLevel;

    public ConfigurationManager(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder() + "/gamelib/" + fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
        reload();
    }

    private void reload() {
        if (file.exists()) {
            this.debugLevel = config.getInt("debug_level");
        } else {
            config.set("debug_level", 0);
            save();
        }
    }

    public int getDebugLevel() {
        return this.debugLevel;
    }

    public void save() {
        try {
            this.config.save(file);
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
