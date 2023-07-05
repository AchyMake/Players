package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

public class PlayerDeath implements Listener {
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerDeath(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        getDatabase().setLocation(event.getEntity(), "death");
        getDatabase().setBoolean(event.getEntity(), "settings.dead", true);
        if (getConfig().getBoolean("deaths.drop-player-head.enable")) {
            if (getConfig().getInt("deaths.drop-player-head.chance") > new Random().nextInt(100)) {
                ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
                skullMeta.setOwningPlayer(event.getEntity());
                skullItem.setItemMeta(skullMeta);
                event.getDrops().add(skullItem);
            }
        }
    }
}