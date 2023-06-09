package net.achymake.players.listeners;

import net.achymake.players.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {
    public PlayerLogin(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getPlayer().getServer().getOnlinePlayers().size() >= event.getPlayer().getServer().getMaxPlayers()) {
            if (event.getPlayer().hasPermission("players.join-full-server")) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
                event.allow();
            }
        }
    }
}