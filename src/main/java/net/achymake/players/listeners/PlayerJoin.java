package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import net.achymake.players.files.Motd;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.MessageFormat;

public class PlayerJoin implements Listener {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public PlayerJoin(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("players.command.reload")) {
            getPlugin().sendUpdate(event.getPlayer());
        }
        if (getDatabase().isVanished(event.getPlayer())) {
            getDatabase().setVanish(event.getPlayer(), true);
            event.setJoinMessage(null);
            getMessage().send(event.getPlayer(), "&6You joined back vanished");
        } else {
            getDatabase().hideVanished(event.getPlayer());
            FileConfiguration config = Players.getInstance().getConfig();
            if (config.getBoolean("connection.join.enable")) {
                event.setJoinMessage(getMessage().addColor(MessageFormat.format(config.getString("connection.join.message"), event.getPlayer().getName())));
                for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                    players.playSound(players, Sound.valueOf(config.getString("connection.join.sound.type")), Float.valueOf(config.getString("connection.join.sound.volume")), Float.valueOf(config.getString("connection.join.sound.pitch")));
                }
            } else {
                if (event.getPlayer().hasPermission("players.join-message")) {
                    event.setJoinMessage(getMessage().addColor(MessageFormat.format(config.getString("connection.join.message"), event.getPlayer().getName())));
                    if (config.getBoolean("connection.join.sound.enable")) {
                        for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                            players.playSound(players, Sound.valueOf(config.getString("connection.join.sound.type")), Float.valueOf(config.getString("connection.join.sound.volume")), Float.valueOf(config.getString("connection.join.sound.pitch")));
                        }
                    }
                } else {
                    event.setJoinMessage(null);
                }
            }
        }
        getDatabase().resetTabList();
        sendMotd(event.getPlayer());
    }
    private void sendMotd(Player player) {
        Database database = Players.getDatabase();
        Motd motd = Players.getMotd();
        if (database.locationExist(player, "quit")) {
            if (motd.motdExist("message-of-the-day")) {
                motd.sendMotd(player, "message-of-the-day");
            }
        } else {
            if (motd.motdExist("welcome")) {
                motd.sendMotd(player, "welcome");
            }
        }
    }
}