package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    public PlayerCommandPreprocess(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("players.exempt.commands"))return;
        for (String disabled : getConfig().getStringList("commands.disable")) {
            if (event.getMessage().startsWith("/" + disabled)) {
                event.setCancelled(true);
            }
        }
    }
}
