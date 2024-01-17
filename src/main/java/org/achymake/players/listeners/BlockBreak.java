package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.text.MessageFormat;

public class BlockBreak implements Listener {
    private final Players plugin;
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public BlockBreak(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isFrozenOrJailed(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            if (!getConfig().getBoolean("notification.enable"))return;
            if (!getConfig().getStringList("notification.block-break").contains(event.getBlock().getType().toString()))return;
            for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                if (players.hasPermission("players.event.block-break.notify")) {
                    for (String messages : getConfig().getStringList("notification.message")) {
                        players.sendMessage(getMessage().addColor(MessageFormat.format(messages, event.getPlayer().getName(), event.getBlock().getType().toString(), event.getBlock().getWorld().getName(), event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY(), event.getBlock().getLocation().getBlockZ())));
                    }
                }
            }
        }
    }
    private boolean isFrozenOrJailed(Player player) {
        return getDatabase().isFrozen(player) || getDatabase().isJailed(player);
    }
}
