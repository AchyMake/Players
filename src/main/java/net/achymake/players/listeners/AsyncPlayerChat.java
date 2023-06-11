package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public AsyncPlayerChat(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat (AsyncPlayerChatEvent event) {
        if (getDatabase().isMuted(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            if (event.getPlayer().hasPermission("players.chatcolor.chat")) {
                event.setMessage(getMessage().addColor(event.getMessage()));
            }
            event.setFormat(getMessage().addColor(getDatabase().prefix(event.getPlayer()) + event.getPlayer().getName() + "&r"  + getDatabase().suffix(event.getPlayer()) + "&r") + ": " + event.getMessage());
        }
    }
}