package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class PlayerDeath implements Listener {
    private final Players plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    public PlayerDeath(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        getDatabase().setLocation(player, "death");
        getDatabase().setBoolean(player, "settings.dead", true);
        if (getConfig().getBoolean("deaths.drop-player-head.enable")) {
            if (getConfig().getInt("deaths.drop-player-head.chance") > new Random().nextInt(100)) {
                event.getDrops().add(getDatabase().getOfflinePlayerHead(player, 1));
            }
        } else {
            if (player.hasPermission("players.death-player-head")) {
                if (getConfig().getInt("deaths.drop-player-head.chance") > new Random().nextInt(100)) {
                    event.getDrops().add(getDatabase().getOfflinePlayerHead(player, 1));
                }
            }
        }
    }
}
