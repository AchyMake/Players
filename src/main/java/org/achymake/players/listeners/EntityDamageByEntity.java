package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public record EntityDamageByEntity(Players plugin) implements Listener {
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByArrow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow) {
            if (!(arrow.getShooter() instanceof Player player))return;
            if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!getUserdata().isPVP(player)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!getUserdata().isPVP(target)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof Player player) {
            if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!getUserdata().isPVP(player)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player,"&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!getUserdata().isPVP(target)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player,"&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof Snowball snowball) {
            if (!(snowball.getShooter() instanceof Player player))return;
            if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!getUserdata().isPVP(player)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!getUserdata().isPVP(target)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof SpectralArrow spectralArrow) {
            if (!(spectralArrow.getShooter() instanceof Player player))return;
            if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!getUserdata().isPVP(player)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!getUserdata().isPVP(target)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof ThrownPotion thrownPotion) {
            if (!(thrownPotion.getShooter() instanceof Player player))return;
            if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!getUserdata().isPVP(player)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!getUserdata().isPVP(target)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof Trident trident) {
            if (!(trident.getShooter() instanceof Player player))return;
            if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!getUserdata().isPVP(player)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!getUserdata().isPVP(target)) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        }
    }
}
