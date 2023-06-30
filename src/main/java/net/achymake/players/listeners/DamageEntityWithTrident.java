package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEntityWithTrident implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public DamageEntityWithTrident(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntityWithTrident(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.TRIDENT))return;
        Trident damager = (Trident) event.getDamager();
        if (damager.getShooter() instanceof Player) {
            Player player = (Player) damager.getShooter();
            if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity().getType().equals(EntityType.PLAYER)) {
                    Player target = (Player) event.getEntity();
                    if (!getDatabase().isPVP(player)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cError:&7 Your pvp is false");
                    } else if (!getDatabase().isPVP(target)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cError:&f " + target.getName() + "&7 pvp is false");
                    }
                }
            }
        }
    }
}