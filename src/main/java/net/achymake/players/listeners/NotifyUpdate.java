package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.version.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NotifyUpdate implements Listener {
    public NotifyUpdate(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onNotifyUpdate(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("players.command.reload"))return;
        new UpdateChecker(Players.getInstance(), 0).sendMessage(event.getPlayer());
    }
}