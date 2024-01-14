package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.text.MessageFormat;

public class BlockPlace implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    public BlockPlace(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isFrozenOrJailed(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            if (!getConfig().getBoolean("notification.enable"))return;
            if (!getConfig().getStringList("notification.block-place").contains(event.getBlockPlaced().getType().toString()))return;
            for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                if (players.hasPermission("players.notify.block-place")) {
                    for (String messages : getConfig().getStringList("notification.message")) {
                        players.sendMessage(Players.addColor(MessageFormat.format(messages, event.getPlayer().getName(), event.getBlockPlaced().getType().toString(), event.getBlock().getWorld().getName(), event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY(), event.getBlock().getLocation().getBlockZ())));
                    }
                }
            }
        }
    }
    private boolean isFrozenOrJailed(Player player) {
        return getDatabase().isFrozen(player) || getDatabase().isJailed(player);
    }
}
