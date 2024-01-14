package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {
    public SignChange(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length; i++) {
            if (!event.getLine(i).contains("&"))return;
            if (!event.getPlayer().hasPermission("players.chatcolor.sign"))return;
            event.setLine(i, Players.addColor(event.getLine(i)));
        }
    }
}