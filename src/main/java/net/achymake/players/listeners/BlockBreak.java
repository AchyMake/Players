package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.text.MessageFormat;

public class BlockBreak implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    public BlockBreak(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (getDatabase().isFrozen(event.getPlayer()) || getDatabase().isJailed(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            if (!getConfig().getBoolean("notification.enable"))return;
            if (!getConfig().getStringList("notification.block-break").contains(event.getBlock().getType().toString()))return;
            for (Player players : event.getPlayer().getServer().getOnlinePlayers()) {
                if (players.hasPermission("players.notify.block-break")) {
                    for (String messages : getConfig().getStringList("notification.message")) {
                        players.sendMessage(Players.addColor(MessageFormat.format(messages, event.getPlayer().getName(), event.getBlock().getType().toString(), event.getBlock().getWorld().getName(), event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY(), event.getBlock().getLocation().getBlockZ())));
                    }
                }
            }
        }
    }
}