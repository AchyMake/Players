package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

public record PlayerDeath(Players plugin) implements Listener {
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        getUserdata().setLocation(player, "death");
        if (!(getConfig().getInt("deaths.drop-player-head.chance") > new Random().nextInt(100)))return;
        if (getConfig().getBoolean("deaths.drop-player-head.enable")) {
            event.getDrops().add(getOfflinePlayerHead(player, 1));
        } else if (player.hasPermission("players.death-player-head")) {
            event.getDrops().add(getOfflinePlayerHead(player, 1));
        }
    }
    private ItemStack getOfflinePlayerHead(OfflinePlayer offlinePlayer, int amount) {
        if (offlinePlayer == null) {
            return new ItemStack(Material.PLAYER_HEAD, amount);
        } else {
            ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, amount);
            SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
            skullMeta.setOwningPlayer(offlinePlayer);
            skullItem.setItemMeta(skullMeta);
            return skullItem;
        }
    }
}
