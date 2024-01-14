package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitWhileVanished implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerQuitWhileVanished(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitWhileVanished(PlayerQuitEvent event) {
        if (!getDatabase().isVanished(event.getPlayer()))return;
        getDatabase().setLocation(event.getPlayer(), "quit");
        getDatabase().getVanished().remove(event.getPlayer());
        event.setQuitMessage(null);
    }
}
