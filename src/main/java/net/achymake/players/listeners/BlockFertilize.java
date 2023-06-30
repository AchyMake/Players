package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class BlockFertilize implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public BlockFertilize(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (!isCancelled(event.getPlayer()))return;
        event.setCancelled(true);
    }
    private boolean isCancelled(Player player) {
        if (getDatabase().isFrozen(player)) {
            return true;
        }
        return getDatabase().isJailed(player);
    }
}