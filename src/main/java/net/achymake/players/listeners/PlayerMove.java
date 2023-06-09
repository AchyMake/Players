package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    public PlayerMove(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Database database = Players.getDatabase();
        if (database.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
        if (database.isVanished(event.getPlayer())) {
            Message message = Players.getMessage();
            message.sendActionBar(event.getPlayer(), "&6&lVanish:&a Enabled");
        }
    }
}