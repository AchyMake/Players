package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockReceiveGameEvent;

public record BlockReceiveGame(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockReceiveGame(BlockReceiveGameEvent event) {
        if (!(event.getEntity() instanceof Player player))return;
        if (!(getUserdata().isFrozen(player) || getUserdata().isJailed(player) || getUserdata().isVanished(player)))return;
        event.setCancelled(true);
    }
}