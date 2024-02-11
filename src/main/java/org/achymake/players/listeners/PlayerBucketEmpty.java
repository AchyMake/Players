package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.text.MessageFormat;

public record PlayerBucketEmpty(Players plugin) implements Listener {
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
            event.setCancelled(true);
        } else {
            if (!getConfig().getBoolean("notification.enable"))return;
            String material = event.getBucket().toString();
            if (!getConfig().getStringList("notification.bucket-empty").contains(material))return;
            String worldName = event.getBlock().getWorld().getName();
            int x = event.getBlock().getX();
            int y = event.getBlock().getY();
            int z = event.getBlock().getZ();
            for (Player players : getServer().getOnlinePlayers()) {
                if (!players.hasPermission("players.event.bucket-empty.notify"))return;
                for (String messages : getConfig().getStringList("notification.message")) {
                    players.sendMessage(getMessage().addColor(MessageFormat.format(messages, player.getName(), material, worldName, x, y, z)));
                }
            }
        }
    }
}
