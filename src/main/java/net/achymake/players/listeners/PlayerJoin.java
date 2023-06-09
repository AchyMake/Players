package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import net.achymake.players.files.Motd;
import net.achymake.players.version.UpdateChecker;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.MessageFormat;

public class PlayerJoin implements Listener {
    public PlayerJoin(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Database database = Players.getDatabase();
        Message message = Players.getMessage();
        if (event.getPlayer().hasPermission("players.command.reload")) {
            new UpdateChecker(Players.getInstance(), 110266).sendMessage(event.getPlayer());
        }
        if (database.isVanished(event.getPlayer())) {
            database.setVanish(event.getPlayer(), true);
            event.setJoinMessage(null);
            message.send(event.getPlayer(), "&6You joined back vanished");
        } else {
            database.hideVanished(event.getPlayer());
            FileConfiguration config = Players.getInstance().getConfig();
            if (config.getBoolean("connection.join.enable")) {
                event.setJoinMessage(message.addColor(MessageFormat.format(config.getString("connection.join.message"), event.getPlayer().getName())));
                for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                    players.playSound(players, Sound.valueOf(config.getString("connection.join.sound.type")), Float.valueOf(config.getString("connection.join.sound.volume")), Float.valueOf(config.getString("connection.join.sound.pitch")));
                }
            } else {
                if (event.getPlayer().hasPermission("players.join-message")) {
                    event.setJoinMessage(message.addColor(MessageFormat.format(config.getString("connection.join.message"), event.getPlayer().getName())));
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
        database.resetTabList();
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