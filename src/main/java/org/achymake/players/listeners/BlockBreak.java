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
import org.bukkit.event.block.BlockBreakEvent;

import java.text.MessageFormat;

public record BlockBreak(Players plugin) implements Listener {
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
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
            event.setCancelled(true);
        } else {
            if (!getConfig().getBoolean("notification.enable"))return;
            Block block = event.getBlock();
            if (!getConfig().getStringList("notification.block-break").contains(block.getType().toString()))return;
            String worldName = block.getWorld().getName();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            for (Player players : getServer().getOnlinePlayers()) {
                if (!players.hasPermission("players.event.block-break.notify"))return;
                for (String messages : getConfig().getStringList("notification.message")) {
                    players.sendMessage(getMessage().addColor(MessageFormat.format(messages, player.getName(), block.getType().toString(), worldName, x, y, z)));
                }
            }
        }
    }
}
