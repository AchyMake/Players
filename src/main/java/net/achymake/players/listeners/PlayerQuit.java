package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
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
    private FileConfiguration getConfig() {
        return Players.getInstance().getConfig();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public PlayerQuit(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        getDatabase().setLocation(event.getPlayer(), "quit");
        if (getDatabase().isVanished(event.getPlayer())) {
            getDatabase().getVanished().remove(event.getPlayer());
            event.setQuitMessage(null);
        } else {
            getDatabase().resetTabList();
            if (getConfig().getBoolean("connection.quit.enable")) {
                event.setQuitMessage(getMessage().addColor(MessageFormat.format(getConfig().getString("connection.quit.message"), event.getPlayer().getName())));
                if (getConfig().getBoolean("connection.quit.sound.enable")) {
                    for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                        players.playSound(players, Sound.valueOf(getConfig().getString("connection.quit.sound.type")), Float.valueOf(getConfig().getString("connection.quit.sound.volume")), Float.valueOf(getConfig().getString("connection.quit.sound.pitch")));
                    }
                }
            } else {
                if (event.getPlayer().hasPermission("players.quit-message")) {
                    event.setQuitMessage(getMessage().addColor(MessageFormat.format(getConfig().getString("connection.quit.message"), event.getPlayer().getName())));
                    if (getConfig().getBoolean("connection.quit.sound.enable")) {
                        for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                            players.playSound(players, Sound.valueOf(getConfig().getString("connection.quit.sound.type")), Float.valueOf(getConfig().getString("connection.quit.sound.volume")), Float.valueOf(getConfig().getString("connection.quit.sound.pitch")));
                        }
                    }
                } else {
                    event.setQuitMessage(null);
                }
            }
        }
        if (getDatabase().getConfig(event.getPlayer()).isString("tpa.from")) {
            Player target = event.getPlayer().getServer().getPlayer(UUID.fromString(getDatabase().getConfig(event.getPlayer()).getString("tpa.from")));
            if (target != null) {
                getDatabase().setString(target, "tpa.sent", null);
                if (event.getPlayer().getServer().getScheduler().isQueued(getDatabase().getConfig(target).getInt("task.tpa"))) {
                    event.getPlayer().getServer().getScheduler().cancelTask(getDatabase().getConfig(target).getInt("task.tpa"));
                    getDatabase().setString(event.getPlayer(), "tpa.from", null);
                }
                getDatabase().setString(target, "task.tpa", null);
            }
        }
        if (getDatabase().getConfig(event.getPlayer()).isString("tpa.sent")) {
            Player target = event.getPlayer().getServer().getPlayer(UUID.fromString(getDatabase().getConfig(event.getPlayer()).getString("tpa.sent")));
            if (target != null) {
                getDatabase().setString(target, "tpa.from", null);
                if (event.getPlayer().getServer().getScheduler().isQueued(getDatabase().getConfig(event.getPlayer()).getInt("task.tpa"))) {
                    event.getPlayer().getServer().getScheduler().cancelTask(getDatabase().getConfig(event.getPlayer()).getInt("task.tpa"));
                    getDatabase().setString(event.getPlayer(), "task.tpa", null);
                }
            }
            getDatabase().setString(event.getPlayer(), "tpa.sent", null);
        }
    }
}
