package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public AsyncPlayerChat(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (getDatabase().isMuted(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            if (event.getPlayer().hasPermission("players.chatcolor.chat")) {
                event.setMessage(Players.addColor(event.getMessage()));
            }
            event.setFormat(getDatabase().prefix(event.getPlayer()) + getDatabase().getDisplayName(event.getPlayer()) + ChatColor.WHITE + getDatabase().suffix(event.getPlayer()) + ChatColor.WHITE + ": " + event.getMessage());
        }
    }
}
