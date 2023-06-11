package net.achymake.players.files;

import me.clip.placeholderapi.PlaceholderAPI;
import net.achymake.players.Players;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Database {
    private final File dataFolder;
    private final HashMap<String, Long> commandCooldown = new HashMap<>();
    private final List<Player> vanished = new ArrayList<>();
    public Database(File dataFolder) {
        this.dataFolder = dataFolder;
    }
    private Players getPlugin() {
        return Players.getInstance();
    }
    private FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }
    public boolean exist(OfflinePlayer offlinePlayer) {
        return new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml").exists();
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml"));
    }
    public void setup(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            if (!getConfig(offlinePlayer).getString("name").equals(offlinePlayer.getName())) {
                File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
                playerConfig.set("name", offlinePlayer.getName());
                try {
                    playerConfig.save(file);
                } catch (IOException e) {
                    Players.getMessage().sendLog(Level.WARNING, e.getMessage());
                }
            }
        } else {
            File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
            playerConfig.set("name", offlinePlayer.getName());
            playerConfig.set("display-name", offlinePlayer.getName());
            playerConfig.set("account", getConfig().getDouble("economy.starting-balance"));
            playerConfig.set("max-homes", getConfig().getInt("homes.default"));
            playerConfig.createSection("homes");
            playerConfig.set("settings.pvp", true);
            playerConfig.set("settings.frozen", false);
            playerConfig.set("settings.jailed", false);
            playerConfig.set("settings.dead", false);
            Location location = randomLocation();
            playerConfig.set("locations.spawn.world", location.getWorld().getName());
            playerConfig.set("locations.spawn.x", location.getX());
            playerConfig.set("locations.spawn.y", location.getY());
            playerConfig.set("locations.spawn.z", location.getZ());
            playerConfig.set("locations.spawn.yaw", location.getYaw());
            playerConfig.set("locations.spawn.pitch", location.getPitch());
            playerConfig.set("locations.recent.world", location.getWorld().getName());
            playerConfig.set("locations.recent.x", location.getX());
            playerConfig.set("locations.recent.y", location.getY());
            playerConfig.set("locations.recent.z", location.getZ());
            playerConfig.set("locations.recent.yaw", location.getYaw());
            playerConfig.set("locations.recent.pitch", location.getPitch());
            try {
                playerConfig.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setInt(OfflinePlayer offlinePlayer, String path, int value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setDouble(OfflinePlayer offlinePlayer, String path, double value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setFloat(OfflinePlayer offlinePlayer, String path, float value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setString(OfflinePlayer offlinePlayer, String path, String value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean homeExist(OfflinePlayer offlinePlayer, String homeName) {
        return getConfig(offlinePlayer).getConfigurationSection("homes").contains(homeName);
    }
    public List<String> getHomes(OfflinePlayer offlinePlayer) {
        return new ArrayList<>(getConfig(offlinePlayer).getConfigurationSection("homes").getKeys(false));
    }
    public void setHome(Player player, String homeName) {
        setString(player, "homes." + homeName + ".world", player.getWorld().getName());
        setDouble(player, "homes." + homeName + ".x", player.getLocation().getX());
        setDouble(player, "homes." + homeName + ".y", player.getLocation().getY());
        setDouble(player, "homes." + homeName + ".z", player.getLocation().getZ());
        setFloat(player, "homes." + homeName + ".yaw", player.getLocation().getYaw());
        setFloat(player, "homes." + homeName + ".pitch", player.getLocation().getPitch());
    }
    public Location getHome(OfflinePlayer offlinePlayer, String homeName) {
        String worldName = getConfig(offlinePlayer).getString("homes." + homeName + ".world");
        double x = getConfig(offlinePlayer).getDouble("homes." + homeName + ".x");
        double y = getConfig(offlinePlayer).getDouble("homes." + homeName + ".y");
        double z = getConfig(offlinePlayer).getDouble("homes." + homeName + ".z");
        float yaw = getConfig(offlinePlayer).getLong("homes." + homeName + ".yaw");
        float pitch = getConfig(offlinePlayer).getLong("homes." + homeName + ".pitch");
        return new Location(Players.getInstance().getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }
    public boolean locationExist(OfflinePlayer offlinePlayer, String locationName) {
        return getConfig(offlinePlayer).getConfigurationSection("locations").contains(locationName);
    }
    public void setLocation(Player player, String locationName) {
        setString(player, "locations." + locationName + ".world", player.getWorld().getName());
        setDouble(player, "locations." + locationName + ".x", player.getLocation().getX());
        setDouble(player, "locations." + locationName + ".y", player.getLocation().getY());
        setDouble(player, "locations." + locationName + ".z", player.getLocation().getZ());
        setFloat(player, "locations." + locationName + ".yaw", player.getLocation().getYaw());
        setFloat(player, "locations." + locationName + ".pitch", player.getLocation().getPitch());
    }
    public void setLocation(OfflinePlayer offlinePlayer, String locationName, Location location) {
        setString(offlinePlayer, "locations." + locationName + ".world", location.getWorld().getName());
        setDouble(offlinePlayer, "locations." + locationName + ".x", location.getX());
        setDouble(offlinePlayer, "locations." + locationName + ".y", location.getY());
        setDouble(offlinePlayer, "locations." + locationName + ".z", location.getZ());
        setFloat(offlinePlayer, "locations." + locationName + ".yaw", location.getYaw());
        setFloat(offlinePlayer, "locations." + locationName + ".pitch", location.getPitch());
    }
    public Location getLocation(OfflinePlayer offlinePlayer, String locationName) {
        String worldName = getConfig(offlinePlayer).getString("locations." + locationName + ".world");
        double x = getConfig(offlinePlayer).getDouble("locations." + locationName + ".x");
        double y = getConfig(offlinePlayer).getDouble("locations." + locationName + ".y");
        double z = getConfig(offlinePlayer).getDouble("locations." + locationName + ".z");
        float yaw = getConfig(offlinePlayer).getLong("locations." + locationName + ".yaw");
        float pitch = getConfig(offlinePlayer).getLong("locations." + locationName + ".pitch");
        return new Location(Players.getInstance().getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }
    public void hideVanished(Player player) {
        if (!vanished.isEmpty()) {
            for (Player vanishedPlayers : vanished) {
                player.hidePlayer(Players.getInstance(), vanishedPlayers);
            }
        }
    }
    public void setVanish(OfflinePlayer offlinePlayer, boolean value) {
        if (value) {
            setBoolean(offlinePlayer,"settings.vanished", true);
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                vanished.add(player);
                for (Player onlinePlayers : getPlugin().getServer().getOnlinePlayers()) {
                    onlinePlayers.hidePlayer(getPlugin(), player);
                }
                player.setAllowFlight(true);
                player.setInvulnerable(true);
                player.setSleepingIgnored(true);
                player.setCollidable(false);
                player.setSilent(true);
                player.setCanPickupItems(false);
                for (Player vanishedPlayers : vanished) {
                    vanishedPlayers.showPlayer(getPlugin(), player);
                    player.showPlayer(getPlugin(), vanishedPlayers);
                }
                resetTabList();
            }
        } else {
            setBoolean(offlinePlayer,"settings.vanished", false);
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                vanished.remove(player);
                for (Player onlinePlayers : getPlugin().getServer().getOnlinePlayers()) {
                    onlinePlayers.showPlayer(getPlugin(), player);
                }
                if (!player.hasPermission("players.command.fly")) {
                    player.setAllowFlight(false);
                }
                player.setInvulnerable(false);
                player.setSleepingIgnored(false);
                player.setCollidable(true);
                player.setSilent(false);
                player.setCanPickupItems(true);
                for (Player vanishedPlayers : vanished) {
                    player.hidePlayer(getPlugin(), vanishedPlayers);
                }
                resetTabList();
            }
        }
    }
    public double getEconomy(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getDouble("account");
    }
    public void addEconomy(OfflinePlayer offlinePlayer, double amount) {
        setDouble(offlinePlayer, "account", amount + getEconomy(offlinePlayer));
    }
    public void removeEconomy(OfflinePlayer offlinePlayer, double amount) {
        setDouble(offlinePlayer, "account", getEconomy(offlinePlayer) - amount);
    }
    public void setEconomy(OfflinePlayer offlinePlayer, double value) {
        setDouble(offlinePlayer, "account", value);
    }
    public void resetEconomy(OfflinePlayer offlinePlayer) {
        setDouble(offlinePlayer, "account", getConfig().getDouble("economy.starting-balance"));
    }
    public String prefix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return PlaceholderAPI.setPlaceholders(player, "%vault_prefix%");
        } else {
            return "";
        }
    }
    public String suffix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return PlaceholderAPI.setPlaceholders(player, "%vault_suffix%");
        } else {
            return "";
        }
    }
    public void resetTabList() {
        if (getConfig().getBoolean("tablist.enable")) {
            for (Player players : getPlugin().getServer().getOnlinePlayers()) {
                players.setPlayerListHeader(addColor(getConfig().getString("tablist.header")));
                players.setPlayerListName(addColor(prefix(players) + players.getName() + suffix(players)));
                players.setPlayerListFooter(addColor(MessageFormat.format(getConfig().getString("tablist.footer"), players.getServer().getOnlinePlayers().size() - vanished.size(), players.getServer().getMaxPlayers())));
            }
        }
    }
    public void teleportBack(Player player) {
        if (locationExist(player, "death")) {
            getLocation(player, "death").getChunk().load();
            sendActionBar(player, "&6Teleporting to&f death location");
            player.teleport(getLocation(player, "death"));
            setString(player, "locations.death", null);
        } else if (locationExist(player, "recent")) {
            getLocation(player, "recent").getChunk().load();
            sendActionBar(player, "&6Teleporting to&f recent location");
            player.teleport(getLocation(player, "recent"));
        } else {
            send(player, "&cRecent location either removed or has never been set");
        }
    }
    public Block highestRandomBlock() {
        String worldName = getConfig().getString("commands.rtp.world");
        int spread = getConfig().getInt("commands.rtp.spread");
        return getPlugin().getServer().getWorld(worldName).getHighestBlockAt(new Random().nextInt(0, spread), new Random().nextInt(0, spread));
    }
    public Location randomLocation() {
        Block block = highestRandomBlock();
        if (block.isLiquid()) {
            return randomLocation();
        } else {
            return block.getLocation().add(0.5, 1, 0.5);
        }
    }
    public boolean isDead(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.dead");
    }
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.pvp");
    }
    public boolean isMuted(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.muted");
    }
    public boolean isFrozen(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.frozen");
    }
    public boolean isJailed(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.jailed");
    }
    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.vanished");
    }
    public boolean isVanished(Player player) {
        return getConfig(player).getBoolean("settings.vanished");
    }
    public HashMap<String, Long> getCommandCooldown() {
        return commandCooldown;
    }
    public List<Player> getVanished() {
        return vanished;
    }
    private void send(CommandSender sender, String message) {
        sender.sendMessage(addColor(message));
    }
    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    private String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}