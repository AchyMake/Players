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
    public DamageEntityWithTrident(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntityWithTrident(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.TRIDENT))return;
        Trident damager = (Trident) event.getDamager();
        if (damager.getShooter() instanceof Player) {
            Player player = (Player) damager.getShooter();
            Database database = Players.getDatabase();
            if (database.isFrozen(player) || database.isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity().getType().equals(EntityType.PLAYER)) {
                    Player target = (Player) event.getEntity();
                    Message message = Players.getMessage();
                    if (!database.isPVP(player)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&cError:&7 Your pvp is false");
                    } else if (!database.isPVP(target)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&cError:&f " + target.getName() + "&7 pvp is false");
                    }
                }
            }
        }
    }
}