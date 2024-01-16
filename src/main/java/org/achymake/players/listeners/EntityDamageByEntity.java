package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public EntityDamageByEntity(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByArrow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow) {
            if (arrow.getShooter() instanceof Player player) {
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
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!getDatabase().isPVP(player)) {
                        event.setCancelled(true);
                        Players.sendActionBar(player,"&cError:&7 Your PVP is Disabled");
                    } else if (!getDatabase().isPVP(target)) {
                        event.setCancelled(true);
                        Players.sendActionBar(player,"&cError:&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageBySnowball(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Snowball snowball) {
            if (snowball.getShooter() instanceof Player player) {
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
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByThrownPotion(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof ThrownPotion thrownPotion) {
            if (thrownPotion.getShooter() instanceof Player player) {
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
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByTrident(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Trident trident) {
            if (trident.getShooter() instanceof Player player) {
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
