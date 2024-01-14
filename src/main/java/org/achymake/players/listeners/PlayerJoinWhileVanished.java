package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinWhileVanished implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerJoinWhileVanished(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinVanished(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!getDatabase().isVanished(player))return;
        getDatabase().setVanish(player, true);
        event.setJoinMessage(null);
        Players.send(player, "&6You joined back vanished");
        getDatabase().sendUpdate(player);
    }
}
