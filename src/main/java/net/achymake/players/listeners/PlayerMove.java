package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public PlayerMove(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (getDatabase().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
        if (getDatabase().isVanished(event.getPlayer())) {
            getMessage().sendActionBar(event.getPlayer(), "&6&lVanish:&a Enabled");
        }
        if (getDatabase().getConfig(event.getPlayer()).getBoolean("settings.coordinates")) {
            getMessage().sendActionBar(event.getPlayer(), "&6&lY:&f " + event.getPlayer().getLocation().getBlockY());
        }
    }
}