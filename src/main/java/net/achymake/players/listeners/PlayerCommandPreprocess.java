package net.achymake.players.listeners;

import net.achymake.players.Players;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {
    private FileConfiguration getConfig() {
        return Players.getInstance().getConfig();
    }
    public PlayerCommandPreprocess(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
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