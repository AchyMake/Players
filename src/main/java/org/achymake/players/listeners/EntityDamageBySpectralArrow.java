package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageBySpectralArrow implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public EntityDamageBySpectralArrow(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageBySpectralArrow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof SpectralArrow spectralArrow) {
            if (spectralArrow.getShooter() instanceof Player player) {
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    event.setCancelled(true);
                } else {
                    if (event.getEntity() instanceof Player target) {
                        if (!getDatabase().isPVP(player)) {
                            event.setCancelled(true);
                            Players.sendActionBar(player, "&cError:&7 Your PVP is Disabled");
                        } else if (!getDatabase().isPVP(target)) {
                            event.setCancelled(true);
                            Players.sendActionBar(player, "&cError:&f " + target.getName() + "&7 PVP is Disabled");
                        }
                    }
                }
            }
        }
    }
}
