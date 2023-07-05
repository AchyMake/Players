package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Spawn;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Spawn getSpawn() {
        return Players.getSpawn();
    }
    public PlayerRespawn(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!getDatabase().isDead(event.getPlayer()))return;
        if (event.getPlayer().hasPermission("players.death-location")) {
            Location location = getDatabase().getLocation(event.getPlayer(),"death");
            String world = location.getWorld().getEnvironment().toString().toLowerCase();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            Players.send(event.getPlayer(), "&6Death location:");
            Players.send(event.getPlayer(), "&6World:&f " + world + "&6 X:&f " + x + "&6 Y:&f " + y + "&6 Z:&f " + z);
        }
        getDatabase().setBoolean(event.getPlayer(), "settings.dead", false);
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        if (getDatabase().locationExist(event.getPlayer(), "spawn")) {
            event.setRespawnLocation(getDatabase().getLocation(event.getPlayer(), "spawn"));
        } else {
            if (getSpawn().locationExist()) {
                event.setRespawnLocation(getSpawn().getLocation());
            }
        }
    }
}