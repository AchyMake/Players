package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

public record PlayerHarvestBlock(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerHarvestBlock(PlayerHarvestBlockEvent event) {
        if (!(getUserdata().isFrozen(event.getPlayer()) || getUserdata().isJailed(event.getPlayer())))return;
        event.setCancelled(true);
    }
}
