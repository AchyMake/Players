package org.achymake.players.files;

import me.clip.placeholderapi.PlaceholderAPI;
import org.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Database {
    private File getDataFolder() {
        return Players.getFolder();
    }
    private final List<Player> vanished = new ArrayList<>();
    private final HashMap<String, Long> commandCooldown = new HashMap<>();
    public boolean hasCooldown(Player player, String command) {
        if (getCommandCooldown().containsKey(command + "-" + player.getUniqueId())) {
            Long timeElapsed = System.currentTimeMillis() - getCommandCooldown().get(command + "-" + player.getUniqueId());
            String cooldownTimer = getConfig().getString("commands.cooldown." + command);
            Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
            return timeElapsed < integer;
        }
        return false;
    }
    public void addCooldown(Player player, String command) {
        if (getCommandCooldown().containsKey(command + "-" + player.getUniqueId())) {
            Long timeElapsed = System.currentTimeMillis() - getCommandCooldown().get(command + "-" + player.getUniqueId());
            String cooldownTimer = getConfig().getString("commands.cooldown." + command);
            Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
            if (timeElapsed > integer) {
                getCommandCooldown().put(command + "-" + player.getUniqueId(), System.currentTimeMillis());
            }
        } else {
            getCommandCooldown().put(command + "-" + player.getUniqueId(), System.currentTimeMillis());
        }
    }
    public String getCooldown(Player player, String command) {
        if (getCommandCooldown().containsKey(command + "-" + player.getUniqueId())) {
            Long timeElapsed = System.currentTimeMillis() - getCommandCooldown().get(command + "-" + player.getUniqueId());
            String cooldownTimer = getConfig().getString("commands.cooldown." + command);
            Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
            if (timeElapsed < integer) {
                long timer = (integer-timeElapsed);
                return String.valueOf(timer).substring(0, String.valueOf(timer).length() - 3);
            }
        } else {
            return "0";
        }
        return "0";
    }
    private Players getPlugin() {
        return Players.getInstance();
    }
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Server getHost() {
        return Players.getHost();
    }
    public boolean exist(OfflinePlayer offlinePlayer) {
        return new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml").exists();
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml"));
    }
    public void setup(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            if (!getConfig(offlinePlayer).getString("name").equals(offlinePlayer.getName())) {
                File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
                playerConfig.set("name", offlinePlayer.getName());
                try {
                    playerConfig.save(file);
                    Players.sendLog(Level.INFO, offlinePlayer.getName() + " has changed name");
                } catch (IOException e) {
                    Players.sendLog(Level.WARNING, e.getMessage());
                }
            }
        } else {
            File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
            playerConfig.set("name", offlinePlayer.getName());
            playerConfig.set("display-name", offlinePlayer.getName());
            playerConfig.set("account", getConfig().getDouble("economy.starting-balance"));
            playerConfig.createSection("homes");
            playerConfig.set("settings.pvp", true);
            playerConfig.set("settings.frozen", false);
            playerConfig.set("settings.jailed", false);
            playerConfig.set("settings.dead", false);
            try {
                playerConfig.save(file);
                Players.sendLog(Level.INFO, offlinePlayer.getName() + ".yml has been created");
            } catch (IOException e) {
                Players.sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
    public void setInt(OfflinePlayer offlinePlayer, String path, int value) {
        File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Players.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setDouble(OfflinePlayer offlinePlayer, String path, double value) {
        File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Players.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setFloat(OfflinePlayer offlinePlayer, String path, float value) {
        File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Players.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setString(OfflinePlayer offlinePlayer, String path, String value) {
        File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Players.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Players.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Players.sendLog(Level.WARNING, e.getMessage());
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
    public String getEconomyFormat(double amount) {
        return getConfig().getString("economy.currency") + new DecimalFormat(getConfig().getString("economy.format")).format(amount);
    }
    public boolean homeExist(OfflinePlayer offlinePlayer, String homeName) {
        return getConfig(offlinePlayer).getConfigurationSection("homes").contains(homeName);
    }
    public List<String> getHomes(OfflinePlayer offlinePlayer) {
        return new ArrayList<>(getConfig(offlinePlayer).getConfigurationSection("homes").getKeys(false));
    }
    public boolean setHome(Player player, String homeName) {
        if (homeExist(player, homeName)) {
            setString(player, "homes." + homeName + ".world", player.getWorld().getName());
            setDouble(player, "homes." + homeName + ".x", player.getLocation().getX());
            setDouble(player, "homes." + homeName + ".y", player.getLocation().getY());
            setDouble(player, "homes." + homeName + ".z", player.getLocation().getZ());
            setFloat(player, "homes." + homeName + ".yaw", player.getLocation().getYaw());
            setFloat(player, "homes." + homeName + ".pitch", player.getLocation().getPitch());
            return true;
        } else {
            for (String rank : getConfig().getConfigurationSection("homes").getKeys(false)) {
                if (player.hasPermission("players.command.sethome.multiple." + rank)) {
                    if (getConfig().getInt("homes." + rank) > getHomes(player).size()) {
                        setString(player, "homes." + homeName + ".world", player.getWorld().getName());
                        setDouble(player, "homes." + homeName + ".x", player.getLocation().getX());
                        setDouble(player, "homes." + homeName + ".y", player.getLocation().getY());
                        setDouble(player, "homes." + homeName + ".z", player.getLocation().getZ());
                        setFloat(player, "homes." + homeName + ".yaw", player.getLocation().getYaw());
                        setFloat(player, "homes." + homeName + ".pitch", player.getLocation().getPitch());
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public Location getHome(OfflinePlayer offlinePlayer, String homeName) {
        String worldName = getConfig(offlinePlayer).getString("homes." + homeName + ".world");
        double x = getConfig(offlinePlayer).getDouble("homes." + homeName + ".x");
        double y = getConfig(offlinePlayer).getDouble("homes." + homeName + ".y");
        double z = getConfig(offlinePlayer).getDouble("homes." + homeName + ".z");
        float yaw = getConfig(offlinePlayer).getLong("homes." + homeName + ".yaw");
        float pitch = getConfig(offlinePlayer).getLong("homes." + homeName + ".pitch");
        return new Location(getPlugin().getServer().getWorld(worldName), x, y, z, yaw, pitch);
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
        return new Location(getPlugin().getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }
    public void hideVanished(Player player) {
        if (!getVanished().isEmpty()) {
            for (Player vanishedPlayers : getVanished()) {
                player.hidePlayer(getPlugin(), vanishedPlayers);
            }
        }
    }
    public void setVanish(OfflinePlayer offlinePlayer, boolean value) {
        if (value) {
            setBoolean(offlinePlayer,"settings.vanished", true);
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                getVanished().add(player);
                if (getConfig(player).getBoolean("settings.coordinates")) {
                    setBoolean(player, "settings.coordinates", false);
                }
                for (Player onlinePlayers : getPlugin().getServer().getOnlinePlayers()) {
                    onlinePlayers.hidePlayer(getPlugin(), player);
                }
                player.setAllowFlight(true);
                player.setInvulnerable(true);
                player.setSleepingIgnored(true);
                player.setCollidable(false);
                player.setSilent(true);
                player.setCanPickupItems(false);
                for (Player vanishedPlayers : getVanished()) {
                    vanishedPlayers.showPlayer(getPlugin(), player);
                    player.showPlayer(getPlugin(), vanishedPlayers);
                }
                resetTabList();
                Players.sendActionBar(player, "&6&lVanish:&a Enabled");
            }
        } else {
            setBoolean(offlinePlayer,"settings.vanished", false);
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                getVanished().remove(player);
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
                for (Player vanishedPlayers : getVanished()) {
                    player.hidePlayer(getPlugin(), vanishedPlayers);
                }
                resetTabList();
                Players.sendActionBar(player, "&6&lVanish:&c Disabled");
            }
        }
    }
    public String prefix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return Players.addColor(PlaceholderAPI.setPlaceholders(player, "%vault_prefix%"));
        } else {
            return "";
        }
    }
    public String getDisplayName(Player player) {
        return getConfig(player).getString("display-name");
    }
    public String suffix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return Players.addColor(PlaceholderAPI.setPlaceholders(player, "%vault_suffix%"));
        } else {
            return "";
        }
    }
    public void resetTabList() {
        if (getConfig().getBoolean("tablist.enable")) {
            for (Player players : getPlugin().getServer().getOnlinePlayers()) {
                players.setPlayerListHeader(Players.addColor(getConfig().getString("tablist.header")));
                players.setPlayerListName(prefix(players) + getDisplayName(players) + suffix(players));
                players.setPlayerListFooter(Players.addColor(MessageFormat.format(getConfig().getString("tablist.footer"), players.getServer().getOnlinePlayers().size() - getVanished().size(), players.getServer().getMaxPlayers())));
            }
        }
    }
    public void teleportBack(Player player) {
        if (locationExist(player, "death")) {
            getLocation(player, "death").getChunk().load();
            Players.sendActionBar(player, "&6Teleporting to&f death location");
            player.teleport(getLocation(player, "death"));
            setString(player, "locations.death", null);
        } else if (locationExist(player, "recent")) {
            getLocation(player, "recent").getChunk().load();
            Players.sendActionBar(player, "&6Teleporting to&f recent location");
            player.teleport(getLocation(player, "recent"));
        } else {
            Players.send(player, "&cRecent location either removed or has never been set");
        }
    }
    public Block highestRandomBlock() {
        return getPlugin().getServer().getWorld(getConfig().getString("commands.rtp.world")).getHighestBlockAt(new Random().nextInt(getConfig().getInt("commands.rtp.spread")), new Random().nextInt(getConfig().getInt("commands.rtp.spread")));
    }
    public Location randomLocation() {
        Block block = highestRandomBlock();
        if (block.isLiquid()) {
            return randomLocation();
        } else {
            return block.getLocation().add(0.5, 1, 0.5);
        }
    }
    public boolean hasJoined(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            return getConfig(offlinePlayer).isConfigurationSection("locations.quit");
        } else {
            return false;
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
    public boolean isBanned(Player player) {
        return getConfig(player).getBoolean("settings.banned");
    }
    public String getBanReason(Player player) {
        return getConfig(player).getString("settings.ban-reason");
    }
    public void sendUpdate(Player player) {
        if (player.hasPermission("players.command.reload")) {
            Players.getUpdate(player);
        }
    }
    public HashMap<String, Long> getCommandCooldown() {
        return commandCooldown;
    }
    public List<Player> getVanished() {
        return vanished;
    }
    public ItemStack getOfflinePlayerHead(OfflinePlayer offlinePlayer, int amount) {
        if (offlinePlayer == null) {
            return new ItemStack(Material.PLAYER_HEAD, amount);
        } else {
            ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, amount);
            SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
            skullMeta.setOwningPlayer(offlinePlayer);
            skullItem.setItemMeta(skullMeta);
            return skullItem;
        }
    }
    public void reload(OfflinePlayer[] offlinePlayers) {
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (exist(offlinePlayer)) {
                File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    Players.sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }
}
