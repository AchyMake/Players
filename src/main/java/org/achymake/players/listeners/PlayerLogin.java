package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Server getHost() {
        return Players.getHost();
    }
    public PlayerLogin(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (getHost().getOnlinePlayers().size() >= getHost().getMaxPlayers()) {
            if (player.hasPermission("players.join-full-server")) {
                if (getDatabase().exist(player)) {
                    if (getDatabase().isBanned(player)) {
                        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getDatabase().getBanReason(player));
                    } else {
                        event.allow();
                        getDatabase().setup(player);
                    }
                } else {
                    event.allow();
                    getDatabase().setup(player);
                }
            }
        } else {
            if (getDatabase().exist(player)) {
                if (getDatabase().isBanned(player)) {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getDatabase().getBanReason(player));
                } else {
                    event.allow();
                    getDatabase().setup(player);
                }
            } else {
                event.allow();
                getDatabase().setup(player);
            }
        }
    }
}
