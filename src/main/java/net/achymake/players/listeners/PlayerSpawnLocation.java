package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Spawn;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocation implements Listener {
    public PlayerSpawnLocation(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        Database database = Players.getDatabase();
        if (!database.exist(event.getPlayer())) {
            database.setup(event.getPlayer());
            Spawn spawn = Players.getSpawn();
            if (spawn.spawnExist()) {
                event.setSpawnLocation(spawn.getSpawn());
            } else {
                event.setSpawnLocation(database.getLocation(event.getPlayer(), "spawn"));
            }
        }
    }
}