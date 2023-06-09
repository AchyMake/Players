package net.achymake.players.files;

import net.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Spawn {
    private File file;
    public Spawn(File dataFolder) {
        this.file = new File(dataFolder, "spawn.yml");
    }
    public boolean exist() {
        return file.exists();
    }
    public FileConfiguration configuration() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public boolean spawnExist() {
        return configuration().isConfigurationSection("spawn");
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
            throw new RuntimeException(e);
        }
    }
    public Location getSpawn() {
        if (spawnExist()) {
            String worldName = configuration().getString("spawn.world");
            double x = configuration().getDouble("spawn.x");
            double y = configuration().getDouble("spawn.y");
            double z = configuration().getDouble("spawn.z");
            float yaw = configuration().getLong("spawn.x");
            float pitch = configuration().getLong("spawn.x");
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
                config.save(file);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        } else {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}