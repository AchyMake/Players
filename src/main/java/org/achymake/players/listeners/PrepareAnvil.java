package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareAnvil implements Listener {
    private final Players plugin;
    private Message getMessage() {
        return plugin.getMessage();
    }
    public PrepareAnvil(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (!event.getView().getPlayer().hasPermission("players.chatcolor.anvil"))return;
        if (event.getResult() == null)return;
        if (!event.getResult().hasItemMeta())return;
        ItemMeta resultMeta = event.getResult().getItemMeta();
        resultMeta.setDisplayName(getMessage().addColor(event.getInventory().getRenameText()));
        event.getResult().setItemMeta(resultMeta);
    }
}
