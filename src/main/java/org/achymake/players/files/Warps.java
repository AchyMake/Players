package org.achymake.players.files;

import org.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Warps {
    private final Players plugin;
    private File getDataFolder() {
        return plugin.getDataFolder();
    }
    private Server getHost() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public Warps(Players plugin) {
        this.plugin = plugin;
    }
    public File getFile() {
        return new File(getDataFolder(), "warps.yml");
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public boolean exist() {
        return getFile().exists();
    }
    public boolean locationExist(String warpName) {
        return getConfig().isConfigurationSection(warpName);
    }
    public void setLocation(String warpName, Location location) {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(warpName + ".world", location.getWorld().getName());
        config.set(warpName + ".x", location.getX());
        config.set(warpName + ".y", location.getY());
        config.set(warpName + ".z", location.getZ());
        config.set(warpName + ".yaw", location.getYaw());
        config.set(warpName + ".pitch", location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public Location getLocation(String warpName) {
        if (locationExist(warpName)) {
            String worldName = getConfig().getString(warpName + ".world");
            double x = getConfig().getDouble(warpName + ".x");
            double y = getConfig().getDouble(warpName + ".y");
            double z = getConfig().getDouble(warpName + ".z");
            float yaw = getConfig().getLong(warpName + ".yaw");
            float pitch = getConfig().getLong(warpName + ".pitch");
            return new Location(getHost().getWorld(worldName), x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }
    public List<String> getWarps() {
        return new ArrayList<>(getConfig().getKeys(false));
    }
    public void delWarp(String warpName) {
        if (locationExist(warpName)) {
            File file = getFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set(warpName, null);
            try {
                config.save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
    public void reload() {
        if (exist()) {
            File file = getFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            File file = getFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
}
