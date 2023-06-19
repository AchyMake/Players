package net.achymake.players.files;

import net.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Jail {
    private File file;
    public Jail(File dataFolder) {
        this.file = new File(dataFolder, "jail.yml");
    }
    public boolean exist() {
        return file.exists();
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public boolean jailExist() {
        return getConfig().isConfigurationSection("jail");
    }
    public void setJail(Location location) {
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
            throw new RuntimeException(e);
        }
    }
    public Location getJail() {
        if (jailExist()) {
            String world = getConfig().getString("jail.world");
            double x = getConfig().getDouble("jail.x");
            double y = getConfig().getDouble("jail.y");
            double z = getConfig().getDouble("jail.z");
            float yaw = getConfig().getLong("jail.yaw");
            float pitch = getConfig().getLong("jail.pitch");
            return new Location(Players.getInstance().getServer().getWorld(world), x, y, z, yaw, pitch);
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
            try {
                config.options().copyDefaults(true);
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}