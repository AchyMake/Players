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

public class Warps {
    private File file;
    public Warps(File dataFolder) {
        this.file = new File(dataFolder, "warps.yml");
    }
    public FileConfiguration configuration() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public boolean exist(String warpName) {
        return configuration().isConfigurationSection(warpName);
    }
    public boolean warpExist(String warpName) {
        return configuration().isConfigurationSection(warpName);
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
            throw new RuntimeException(e);
        }
    }
    public Location getWarp(String warpName) {
        if (warpExist(warpName)) {
            String worldName = configuration().getString(warpName + ".world");
            double x = configuration().getDouble(warpName + ".x");
            double y = configuration().getDouble(warpName + ".y");
            double z = configuration().getDouble(warpName + ".z");
            float yaw = configuration().getLong(warpName + ".yaw");
            float pitch = configuration().getLong(warpName + ".pitch");
            return new Location(Players.getInstance().getServer().getWorld(worldName), x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }
    public List<String> getWarps() {
        return new ArrayList<>(configuration().getKeys(false));
    }
    public void delWarp(String warpName) {
        if (warpExist(warpName)) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set(warpName, null);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void reload() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            try {
                config.load(file);
                config.save(file);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        } else {
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}