package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.text.MessageFormat;

public record BlockPlace(Players plugin) implements Listener {
    private FileConfiguration getConfig() {
        return plugin().getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
            event.setCancelled(true);
        } else {
            if (!getConfig().getBoolean("notification.enable"))return;
            Block block = event.getBlockPlaced();
            if (!getConfig().getStringList("notification.block-place").contains(block.getType().toString()))return;
            String worldName = block.getWorld().getName();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            for (Player players : getServer().getOnlinePlayers()) {
                if (!players.hasPermission("players.event.block-place.notify"))return;
                for (String messages : getConfig().getStringList("notification.message")) {
                    players.sendMessage(getMessage().addColor(MessageFormat.format(messages, player.getName(), block.getType().toString(), worldName, x, y, z)));
                }
            }
        }
    }
}
