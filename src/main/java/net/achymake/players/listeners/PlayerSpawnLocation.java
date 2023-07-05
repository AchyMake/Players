package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Spawn;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocation implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Spawn getSpawn() {
        return Players.getSpawn();
    }
    public PlayerSpawnLocation(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        if (!getDatabase().exist(event.getPlayer())) {
            getDatabase().setup(event.getPlayer());
            if (getSpawn().locationExist()) {
                event.setSpawnLocation(getSpawn().getLocation());
            } else {
                event.setSpawnLocation(getDatabase().getLocation(event.getPlayer(), "spawn"));
            }
        }
    }
}