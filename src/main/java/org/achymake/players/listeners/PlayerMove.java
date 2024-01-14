package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerMove(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (getDatabase().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
        if (getDatabase().isVanished(event.getPlayer())) {
            Players.sendActionBar(event.getPlayer(), "&6&lVanish:&a Enabled");
        }
        if (getDatabase().getConfig(event.getPlayer()).getBoolean("settings.coordinates")) {
            Players.sendActionBar(event.getPlayer(), "&6&lY:&f " + event.getPlayer().getLocation().getBlockY());
        }
    }
}
