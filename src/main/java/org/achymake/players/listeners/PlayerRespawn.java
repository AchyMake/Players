package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Spawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        if (!getDatabase().isDead(player))return;
        if (player.hasPermission("players.death-location")) {
            Location location = getDatabase().getLocation(player,"death");
            String world = location.getWorld().getEnvironment().toString().toLowerCase();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            Players.send(player, "&6Death location:");
            Players.send(player, "&6World:&f " + world + "&6 X:&f " + x + "&6 Y:&f " + y + "&6 Z:&f " + z);
        }
        getDatabase().setBoolean(player, "settings.dead", false);
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        if (getDatabase().locationExist(player, "home")) {
            event.setRespawnLocation(getDatabase().getLocation(player, "home"));
        } else {
            if (getSpawn().locationExist()) {
                event.setRespawnLocation(getSpawn().getLocation());
            }
        }
    }
}
