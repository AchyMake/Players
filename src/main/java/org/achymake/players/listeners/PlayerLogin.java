package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public record PlayerLogin(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (getServer().getOnlinePlayers().size() >= getServer().getMaxPlayers()) {
            if (player.hasPermission("players.event.login.full-server")) {
                if (getUserdata().exist(player)) {
                    if (getUserdata().isBanned(player)) {
                        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getUserdata().getBanReason(player));
                    } else {
                        event.allow();
                        getUserdata().setup(player);
                    }
                } else {
                    event.allow();
                    getUserdata().setup(player);
                }
            }
        } else {
            if (getUserdata().exist(player)) {
                if (getUserdata().isBanned(player)) {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getUserdata().getBanReason(player));
                } else {
                    event.allow();
                    getUserdata().setup(player);
                }
            } else {
                event.allow();
                getUserdata().setup(player);
            }
        }
    }
}