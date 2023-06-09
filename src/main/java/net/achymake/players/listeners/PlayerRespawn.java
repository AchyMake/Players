package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
    public PlayerRespawn(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Database database = Players.getDatabase();
        if (!database.isDead(event.getPlayer()))return;
        if (event.getPlayer().hasPermission("players.death-location")) {
            Message message = Players.getMessage();
            Location location = database.getLocation(event.getPlayer(),"death");
            String world = location.getWorld().getEnvironment().toString().toLowerCase();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            message.send(event.getPlayer(), "&6Death location:");
            message.send(event.getPlayer(), "&6World:&f " + world + "&6 X:&f " + x + "&6 Y:&f " + y + "&6 Z:&f " + z);
        }
        database.setBoolean(event.getPlayer(), "settings.dead", false);
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        event.setRespawnLocation(database.getLocation(event.getPlayer(), "spawn"));
    }
}