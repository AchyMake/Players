package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractPhysical implements Listener {
    private final Players plugin;
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    public PlayerInteractPhysical(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractPhysical(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL))return;
        if (event.getClickedBlock() == null)return;
        if (isFrozenOrJailedOrVanished(event.getPlayer())) {
            event.setCancelled(true);
        } else if (getConfig().getBoolean("disable-trample."+event.getClickedBlock().getType())) {
            event.setCancelled(true);
        }
    }
    private boolean isFrozenOrJailedOrVanished(Player player) {
        return getDatabase().isFrozen(player) || getDatabase().isJailed(player) || getDatabase().isVanished(player);
    }
}