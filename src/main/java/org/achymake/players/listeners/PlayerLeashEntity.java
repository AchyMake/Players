package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;

public class PlayerLeashEntity implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerLeashEntity(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        if (getDatabase().isFrozen(event.getPlayer()) || getDatabase().isJailed(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
