package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.MessageFormat;
import java.util.UUID;

public class PlayerQuit implements Listener {
    private final Players plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    private Server getHost() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public PlayerQuit(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getDatabase().setLocation(player, "quit");
        if (getDatabase().getConfig(player).isString("tpa.from")) {
            Player target = getHost().getPlayer(UUID.fromString(getDatabase().getConfig(player).getString("tpa.from")));
            if (target != null) {
                getDatabase().setString(target, "tpa.sent", null);
                if (getHost().getScheduler().isQueued(getDatabase().getConfig(target).getInt("task.tpa"))) {
                    getHost().getScheduler().cancelTask(getDatabase().getConfig(target).getInt("task.tpa"));
                    getDatabase().setString(player, "tpa.from", null);
                }
                getDatabase().setString(target, "task.tpa", null);
            }
        } else if (getDatabase().getConfig(player).isString("tpa.sent")) {
            Player target = getHost().getPlayer(UUID.fromString(getDatabase().getConfig(player).getString("tpa.sent")));
            if (target != null) {
                getDatabase().setString(target, "tpa.from", null);
                if (getHost().getScheduler().isQueued(getDatabase().getConfig(player).getInt("task.tpa"))) {
                    getHost().getScheduler().cancelTask(getDatabase().getConfig(player).getInt("task.tpa"));
                    getDatabase().setString(player, "task.tpa", null);
                }
            }
            getDatabase().setString(player, "tpa.sent", null);
        }
        if (getDatabase().isVanished(player)) {
            getDatabase().getVanished().remove(player);
            event.setQuitMessage(null);
        } else {
            if (getConfig().getBoolean("connection.quit.enable")) {
                event.setQuitMessage(getMessage().addColor(MessageFormat.format(getConfig().getString("connection.quit.message"), player.getName())));
                if (getConfig().getBoolean("connection.quit.sound.enable")) {
                    for (Player players : getHost().getOnlinePlayers()) {
                        players.playSound(players, Sound.valueOf(getConfig().getString("connection.quit.sound.type")), Float.valueOf(getConfig().getString("connection.quit.sound.volume")), Float.valueOf(getConfig().getString("connection.quit.sound.pitch")));
                    }
                }
            } else {
                if (player.hasPermission("players.quit-message")) {
                    event.setQuitMessage(getMessage().addColor(MessageFormat.format(getConfig().getString("connection.quit.message"), player.getName())));
                    if (getConfig().getBoolean("connection.quit.sound.enable")) {
                        for (Player players : getHost().getOnlinePlayers()) {
                            players.playSound(players, Sound.valueOf(getConfig().getString("connection.quit.sound.type")), Float.valueOf(getConfig().getString("connection.quit.sound.volume")), Float.valueOf(getConfig().getString("connection.quit.sound.pitch")));
                        }
                    }
                } else {
                    event.setQuitMessage(null);
                }
            }
        }
        getDatabase().resetTabList();
    }
}
