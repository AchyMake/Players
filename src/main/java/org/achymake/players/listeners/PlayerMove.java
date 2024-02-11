package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

public record PlayerMove(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null)return;
        Player player = event.getPlayer();
        if (getUserdata().isFrozen(player)) {
            if (event.getFrom().getX() != event.getTo().getX()) {
                event.setCancelled(true);
            }
            if (event.getFrom().getY() != event.getTo().getY()) {
                event.setCancelled(true);
            }
            if (event.getFrom().getX() != event.getTo().getX()) {
                event.setCancelled(true);
            }
        } else if (getUserdata().hasTaskID(player, "teleport")) {
            if (event.getFrom().getX() != event.getTo().getX()) {
                hasChangedPosition(event.getPlayer());
            }
            if (event.getFrom().getY() != event.getTo().getY()) {
                hasChangedPosition(event.getPlayer());
            }
            if (event.getFrom().getX() != event.getTo().getX()) {
                hasChangedPosition(event.getPlayer());
            }
        }
    }
    private void hasChangedPosition(Player player) {
        if (getUserdata().hasTaskID(player, "teleport")) {
            getMessage().sendActionBar(player, "&cYou moved before teleporting!");
            getScheduler().cancelTask(getUserdata().getTaskID(player, "teleport"));
            getUserdata().removeTaskID(player, "teleport");
        }
    }
}
