package org.achymake.players.data;

import org.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public record Jail(Players plugin) {
    private File getDataFolder() {
        return plugin.getDataFolder();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public boolean exist() {
        return getFile().exists();
    }
    public File getFile() {
        return new File(getDataFolder(), "jail.yml");
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public boolean locationExist() {
        return getConfig().isConfigurationSection("jail");
    }
    public void setLocation(Location location) {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("jail.world",location.getWorld().getName());
        config.set("jail.x",location.getX());
        config.set("jail.y",location.getY());
        config.set("jail.z",location.getZ());
        config.set("jail.yaw",location.getYaw());
        config.set("jail.pitch",location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public Location getLocation() {
        if (locationExist()) {
            String world = getConfig().getString("jail.world");
            double x = getConfig().getDouble("jail.x");
            double y = getConfig().getDouble("jail.y");
            double z = getConfig().getDouble("jail.z");
            float yaw = getConfig().getLong("jail.yaw");
            float pitch = getConfig().getLong("jail.pitch");
            return new Location(getServer().getWorld(world), x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }
    public void reload() {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (exist()) {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
}
