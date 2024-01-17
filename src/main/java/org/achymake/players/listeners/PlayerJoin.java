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
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.MessageFormat;

public class PlayerJoin implements Listener {
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
    public PlayerJoin(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getDatabase().isVanished(player)) {
            getDatabase().setVanish(player, true);
            getMessage().send(player, "&6You joined back vanished");
            getDatabase().sendUpdate(player);
            event.setJoinMessage(null);
        } else {
            getDatabase().hideVanished(player);
            sendMotd(player);
            if (getConfig().getBoolean("connection.join.enable")) {
                event.setJoinMessage(getMessage().addColor(MessageFormat.format(getConfig().getString("connection.join.message"), player.getName())));
                for (Player players : getHost().getOnlinePlayers()) {
                    players.playSound(players, Sound.valueOf(getConfig().getString("connection.join.sound.type")), Float.valueOf(getConfig().getString("connection.join.sound.volume")), Float.valueOf(getConfig().getString("connection.join.sound.pitch")));
                }
            } else {
                if (player.hasPermission("players.event.join.message")) {
                    event.setJoinMessage(getMessage().addColor(MessageFormat.format(getConfig().getString("connection.join.message"), player.getName())));
                    if (getConfig().getBoolean("connection.join.sound.enable")) {
                        for (Player players : getHost().getOnlinePlayers()) {
                            players.playSound(players, Sound.valueOf(getConfig().getString("connection.join.sound.type")), Float.valueOf(getConfig().getString("connection.join.sound.volume")), Float.valueOf(getConfig().getString("connection.join.sound.pitch")));
                        }
                    }
                } else {
                    event.setJoinMessage(null);
                }
            }
        }
        getDatabase().resetTabList();
        getDatabase().sendUpdate(player);
    }
    private void sendMotd(Player player) {
        if (getDatabase().hasJoined(player)) {
            getDatabase().sendMotd(player, "welcome-back");
        } else {
            getDatabase().sendMotd(player, "welcome");
        }
    }
}
