package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEntityWithArrow implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public DamageEntityWithArrow(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntityWithArrow(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.ARROW))return;
        Arrow damager = (Arrow) event.getDamager();
        if (damager.getShooter() instanceof Player) {
            Player player = (Player) damager.getShooter();
            if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity().getType().equals(EntityType.PLAYER)) {
                    Player target = (Player) event.getEntity();
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