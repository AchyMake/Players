package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveWhileFrozen implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerMoveWhileFrozen(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMoveWhileFrozen(PlayerMoveEvent event) {
        if (!getDatabase().isFrozen(event.getPlayer()))return;
        event.setCancelled(true);
    }
}