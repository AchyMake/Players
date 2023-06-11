package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

public class PlayerDeath implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public PlayerDeath(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        getDatabase().setLocation(player, "death");
        getDatabase().setBoolean(player, "settings.dead", true);
        FileConfiguration config = Players.getInstance().getConfig();
        if (config.getBoolean("deaths.drop-player-head.enable")) {
            if (config.getInt("deaths.drop-player-head.chance") > new Random().nextInt(100)) {
                ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
                skullMeta.setOwningPlayer(player);
                skullItem.setItemMeta(skullMeta);
                event.getDrops().add(skullItem);
            }
        }
    }
}