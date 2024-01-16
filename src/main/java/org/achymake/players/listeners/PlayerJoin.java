package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
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
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Server getHost() {
        return Players.getHost();
    }
    public PlayerJoin(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinVanished(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!getDatabase().isVanished(player))return;
        getDatabase().setVanish(player, true);
        event.setJoinMessage(null);
        Players.send(player, "&6You joined back vanished");
        getDatabase().sendUpdate(player);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getDatabase().isVanished(player))return;
        getDatabase().hideVanished(player);
        if (getConfig().getBoolean("connection.join.enable")) {
            event.setJoinMessage(Players.addColor(MessageFormat.format(getConfig().getString("connection.join.message"), player.getName())));
            for (Player players : getHost().getOnlinePlayers()) {
                players.playSound(players, Sound.valueOf(getConfig().getString("connection.join.sound.type")), Float.valueOf(getConfig().getString("connection.join.sound.volume")), Float.valueOf(getConfig().getString("connection.join.sound.pitch")));
            }
        } else {
            if (player.hasPermission("players.join-message")) {
                event.setJoinMessage(Players.addColor(MessageFormat.format(getConfig().getString("connection.join.message"), player.getName())));
                if (getConfig().getBoolean("connection.join.sound.enable")) {
                    for (Player players : getHost().getOnlinePlayers()) {
                        players.playSound(players, Sound.valueOf(getConfig().getString("connection.join.sound.type")), Float.valueOf(getConfig().getString("connection.join.sound.volume")), Float.valueOf(getConfig().getString("connection.join.sound.pitch")));
                    }
                }
            } else {
                event.setJoinMessage(null);
            }
        }
        sendMotd(player);
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
