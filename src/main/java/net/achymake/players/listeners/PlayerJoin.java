package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
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
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Motd getMotd() {
        return Players.getMotd();
    }
    public PlayerJoin(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("players.command.reload")) {
            Players.getUpdate(event.getPlayer());
        }
        if (getDatabase().isVanished(event.getPlayer())) {
            getDatabase().setVanish(event.getPlayer(), true);
            event.setJoinMessage(null);
            Players.send(event.getPlayer(), "&6You joined back vanished");
        } else {
            getDatabase().hideVanished(event.getPlayer());
            if (getConfig().getBoolean("connection.join.enable")) {
                event.setJoinMessage(Players.addColor(MessageFormat.format(getConfig().getString("connection.join.message"), event.getPlayer().getName())));
                for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                    players.playSound(players, Sound.valueOf(getConfig().getString("connection.join.sound.type")), Float.valueOf(getConfig().getString("connection.join.sound.volume")), Float.valueOf(getConfig().getString("connection.join.sound.pitch")));
                }
            } else {
                if (event.getPlayer().hasPermission("players.join-message")) {
                    event.setJoinMessage(Players.addColor(MessageFormat.format(getConfig().getString("connection.join.message"), event.getPlayer().getName())));
                    if (getConfig().getBoolean("connection.join.sound.enable")) {
                        for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                            players.playSound(players, Sound.valueOf(getConfig().getString("connection.join.sound.type")), Float.valueOf(getConfig().getString("connection.join.sound.volume")), Float.valueOf(getConfig().getString("connection.join.sound.pitch")));
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
        if (getDatabase().locationExist(player, "quit")) {
            if (getMotd().motdExist("message-of-the-day")) {
                getMotd().sendMotd(player, "message-of-the-day");
            }
        } else {
            if (getMotd().motdExist("welcome")) {
                getMotd().sendMotd(player, "welcome");
            }
        }
    }
}