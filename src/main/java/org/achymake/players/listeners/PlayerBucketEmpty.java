package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.text.MessageFormat;

public class PlayerBucketEmpty implements Listener {
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerBucketEmpty(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (getDatabase().isFrozen(event.getPlayer()) || getDatabase().isJailed(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            if (!getConfig().getBoolean("notification.enable"))return;
            if (!getConfig().getStringList("notification.bucket-empty").contains(event.getBucket().toString()))return;
            for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                if (players.hasPermission("players.notify.bucket-empty")) {
                    for (String messages : getConfig().getStringList("notification.message")) {
                        players.sendMessage(Players.addColor(MessageFormat.format(messages, event.getPlayer().getName(), event.getBucket().toString(), event.getBlock().getWorld().getName(), event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY(), event.getBlock().getLocation().getBlockZ())));
                    }
                }
            }
        }
    }
}
