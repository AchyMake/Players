package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public record PlayerShearEntity(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        if (!(getUserdata().isFrozen(player) || getUserdata().isJailed(player)))return;
        event.setCancelled(true);
    }
}