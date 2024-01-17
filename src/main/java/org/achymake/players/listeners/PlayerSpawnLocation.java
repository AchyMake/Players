package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Spawn;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocation implements Listener {
    private final Players plugin;
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    private Spawn getSpawn() {
        return plugin.getSpawn();
    }
    public PlayerSpawnLocation(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        if (!getDatabase().exist(event.getPlayer())) {
            if (getSpawn().locationExist()) {
                event.setSpawnLocation(getSpawn().getLocation());
            }
        }
    }
}
