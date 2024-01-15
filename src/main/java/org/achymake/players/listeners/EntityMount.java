package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;

public class EntityMount implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public EntityMount(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityMount(EntityMountEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER))return;
        if (event.getMount().getType().equals(EntityType.ARMOR_STAND))return;
        Player player = (Player) event.getEntity();
        if (!isFrozenOrJailed(player))return;
        event.setCancelled(true);
    }
    private boolean isFrozenOrJailed(Player player) {
        return getDatabase().isFrozen(player) || getDatabase().isJailed(player);
    }
}
