package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitWhileTPA implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerQuitWhileTPA(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitWhileTPA(PlayerQuitEvent event) {
        if (getDatabase().getConfig(event.getPlayer()).isString("tpa.from")) {
            Player target = event.getPlayer().getServer().getPlayer(UUID.fromString(getDatabase().getConfig(event.getPlayer()).getString("tpa.from")));
            if (target != null) {
                getDatabase().setString(target, "tpa.sent", null);
                if (event.getPlayer().getServer().getScheduler().isQueued(getDatabase().getConfig(target).getInt("task.tpa"))) {
                    event.getPlayer().getServer().getScheduler().cancelTask(getDatabase().getConfig(target).getInt("task.tpa"));
                    getDatabase().setString(event.getPlayer(), "tpa.from", null);
                }
                getDatabase().setString(target, "task.tpa", null);
            }
        } else if (getDatabase().getConfig(event.getPlayer()).isString("tpa.sent")) {
            Player target = event.getPlayer().getServer().getPlayer(UUID.fromString(getDatabase().getConfig(event.getPlayer()).getString("tpa.sent")));
            if (target != null) {
                getDatabase().setString(target, "tpa.from", null);
                if (event.getPlayer().getServer().getScheduler().isQueued(getDatabase().getConfig(event.getPlayer()).getInt("task.tpa"))) {
                    event.getPlayer().getServer().getScheduler().cancelTask(getDatabase().getConfig(event.getPlayer()).getInt("task.tpa"));
                    getDatabase().setString(event.getPlayer(), "task.tpa", null);
                }
            }
            getDatabase().setString(event.getPlayer(), "tpa.sent", null);
        }
    }
}
