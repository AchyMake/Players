package net.achymake.players.files;

import net.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Spawn {
    private final File file;
    public Spawn(File dataFolder) {
        this.file = new File(dataFolder, "spawn.yml");
    }
    public boolean exist() {
        return file.exists();
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public boolean spawnExist() {
        return getConfig().isConfigurationSection("spawn");
    }
    public void setSpawn(Location location) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            Players.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public Location getSpawn() {
        if (spawnExist()) {
            String worldName = getConfig().getString("spawn.world");
            double x = getConfig().getDouble("spawn.x");
            double y = getConfig().getDouble("spawn.y");
            double z = getConfig().getDouble("spawn.z");
            float yaw = getConfig().getLong("spawn.yaw");
            float pitch = getConfig().getLong("spawn.pitch");
            return new Location(Players.getInstance().getServer().getWorld(worldName), x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }
    public void reload() {
        if (exist()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                Players.sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                Players.sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
}