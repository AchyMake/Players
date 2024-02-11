package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Spawn;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public record PlayerSpawnLocation(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Spawn getSpawn() {
        return plugin.getSpawn();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        if (getUserdata().exist(player))return;
        if (!getSpawn().locationExist())return;
        event.setSpawnLocation(getSpawn().getLocation());
    }
}
