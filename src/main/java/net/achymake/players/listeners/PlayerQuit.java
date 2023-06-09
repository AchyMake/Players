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
    public PlayerQuit(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Database database = Players.getDatabase();
        Message message = Players.getMessage();
        database.setLocation(event.getPlayer(), "quit");
        if (database.isVanished(event.getPlayer())) {
            database.getVanished().remove(event.getPlayer());
            event.setQuitMessage(null);
        } else {
            database.resetTabList();
            FileConfiguration config = Players.getInstance().getConfig();
            if (config.getBoolean("connection.quit.enable")) {
                event.setQuitMessage(message.addColor(MessageFormat.format(config.getString("connection.quit.message"), event.getPlayer().getName())));
                if (config.getBoolean("connection.quit.sound.enable")) {
                    for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                        players.playSound(players, Sound.valueOf(config.getString("connection.quit.sound.type")), Float.valueOf(config.getString("connection.quit.sound.volume")), Float.valueOf(config.getString("connection.quit.sound.pitch")));
                    }
                }
            } else {
                if (event.getPlayer().hasPermission("players.quit-message")) {
                    event.setQuitMessage(message.addColor(MessageFormat.format(config.getString("connection.quit.message"), event.getPlayer().getName())));
                    if (config.getBoolean("connection.quit.sound.enable")) {
                        for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                            players.playSound(players, Sound.valueOf(config.getString("connection.quit.sound.type")), Float.valueOf(config.getString("connection.quit.sound.volume")), Float.valueOf(config.getString("connection.quit.sound.pitch")));
                        }
                    }
                } else {
                    event.setQuitMessage(null);
                }
            }
        }
        if (database.getConfig(event.getPlayer()).isString("tpa.from")) {
            Player target = event.getPlayer().getServer().getPlayer(UUID.fromString(database.getConfig(event.getPlayer()).getString("tpa.from")));
            if (target != null) {
                database.setString(target, "tpa.sent", null);
                if (event.getPlayer().getServer().getScheduler().isQueued(database.getConfig(target).getInt("task.tpa"))) {
                    event.getPlayer().getServer().getScheduler().cancelTask(database.getConfig(target).getInt("task.tpa"));
                    database.setString(event.getPlayer(), "tpa.from", null);
                }
                database.setString(target, "task.tpa", null);
            }
        }
        if (database.getConfig(event.getPlayer()).isString("tpa.sent")) {
            Player target = event.getPlayer().getServer().getPlayer(UUID.fromString(database.getConfig(event.getPlayer()).getString("tpa.sent")));
            if (target != null) {
                database.setString(target, "tpa.from", null);
                if (event.getPlayer().getServer().getScheduler().isQueued(database.getConfig(event.getPlayer()).getInt("task.tpa"))) {
                    event.getPlayer().getServer().getScheduler().cancelTask(database.getConfig(event.getPlayer()).getInt("task.tpa"));
                    database.setString(event.getPlayer(), "task.tpa", null);
                }
            }
            database.setString(event.getPlayer(), "tpa.sent", null);
        }
    }
}
