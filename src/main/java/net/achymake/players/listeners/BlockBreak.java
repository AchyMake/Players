package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    public BlockBreak(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
         if (!isCancelled(event.getPlayer()))return;
        event.setCancelled(true);
    }
    private boolean isCancelled(Player player) {
        if (getDatabase().isFrozen(player)) {
            return true;
        } else return getDatabase().isJailed(player);
    }
}