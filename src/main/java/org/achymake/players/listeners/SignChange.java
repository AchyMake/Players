package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {
    private final Players plugin;
    private Message getMessage() {
        return plugin.getMessage();
    }
    public SignChange(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length; i++) {
            if (!event.getLine(i).contains("&"))return;
            if (!event.getPlayer().hasPermission("players.event.signs.color"))return;
            event.setLine(i, getMessage().addColor(event.getLine(i)));
        }
    }
}