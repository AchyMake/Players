package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.text.MessageFormat;

public class PlayerBucketEmpty implements Listener {
    private FileConfiguration getConfig() {
        return Players.getInstance().getConfig();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public PlayerBucketEmpty(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (getDatabase().isFrozen(event.getPlayer()) || getDatabase().isJailed(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            if (getConfig().getBoolean("notification.enable")) {
                if (getConfig().getStringList("notification.bucket-empty").contains(event.getBucket().toString())) {
                    for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                        if (players.hasPermission("players.notify.bucket-empty")) {
                            for (String messages : getConfig().getStringList("notification.message")) {
                                players.sendMessage(getMessage().addColor(MessageFormat.format(messages, event.getPlayer().getName(), event.getBucket().toString(), event.getBlock().getWorld().getName(), event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY(), event.getBlock().getLocation().getBlockZ())));
                            }
                        }
                    }
                }
            }
        }
    }
}