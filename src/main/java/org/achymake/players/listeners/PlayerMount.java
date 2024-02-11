package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;

public record PlayerMount(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMount(EntityMountEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER))return;
        if (event.getMount().getType().equals(EntityType.ARMOR_STAND))return;
        Player player = (Player) event.getEntity();
        if (!(getUserdata().isFrozen(player) || getUserdata().isJailed(player)))return;
        event.setCancelled(true);
    }
}
