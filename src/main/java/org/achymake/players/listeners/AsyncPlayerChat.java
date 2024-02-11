package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public record AsyncPlayerChat(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (getUserdata().isMuted(player)) {
            event.setCancelled(true);
        } else {
            String prefix = getUserdata().prefix(player);
            String displayName = getUserdata().getDisplayName(player);
            String suffix = getUserdata().suffix(player);
            String output = event.getMessage();
            if (player.isOp()) {
                event.setFormat(prefix + getMessage().addColor("&c" + player.getName() + "&f") + suffix + ChatColor.WHITE + ": " + getMessage().addColor(output));
            } else {
                if (player.hasPermission("players.event.chat.color")) {
                    event.setFormat(prefix + displayName + suffix + ChatColor.WHITE + ": " + getMessage().addColor(output));
                } else {
                    event.setFormat(prefix + displayName + suffix + ChatColor.WHITE + ": " + output);
                }
            }
        }
    }
}
