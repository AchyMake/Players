package net.achymake.players.files;

import net.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Warps {
    private final File file;
    public Warps(File dataFolder) {
        this.file = new File(dataFolder, "warps.yml");
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public boolean exist() {
        return file.exists();
    }
    public boolean warpExist(String warpName) {
        return getConfig().isConfigurationSection(warpName);
    }
    public void setWarp(String warpName, Location location) {
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
    public Location getWarp(String warpName) {
        if (warpExist(warpName)) {
            String worldName = getConfig().getString(warpName + ".world");
            double x = getConfig().getDouble(warpName + ".x");
            double y = getConfig().getDouble(warpName + ".y");
            double z = getConfig().getDouble(warpName + ".z");
            float yaw = getConfig().getLong(warpName + ".yaw");
            float pitch = getConfig().getLong(warpName + ".pitch");
            return new Location(Players.getInstance().getServer().getWorld(worldName), x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }
    public List<String> getWarps() {
        return new ArrayList<>(getConfig().getKeys(false));
    }
    public void delWarp(String warpName) {
        if (warpExist(warpName)) {
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
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
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