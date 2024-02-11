package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public record PlayerTeleport(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        PlayerTeleportEvent.TeleportCause teleportCause = event.getCause();
        if (!(teleportCause.equals(PlayerTeleportEvent.TeleportCause.COMMAND) || teleportCause.equals(PlayerTeleportEvent.TeleportCause.PLUGIN)))return;
        getUserdata().setLocation(event.getPlayer(), "recent");
    }
}
