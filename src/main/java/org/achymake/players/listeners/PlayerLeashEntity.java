package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;

public record PlayerLeashEntity(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();
        if (getUserdata().isFrozen(player)) {
            event.setCancelled(true);
            getMessage().send(player, "&c&lHey!&7 Sorry, but you are not allowed to do that while frozen");
        } else if (getUserdata().isJailed(player)) {
            event.setCancelled(true);
            getMessage().send(player, "&c&lHey!&7 Sorry, but you are not allowed to do that while jailed");
        }
    }
}
