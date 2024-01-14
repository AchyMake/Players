package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareAnvil implements Listener {
    public PrepareAnvil(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (!event.getView().getPlayer().hasPermission("players.chatcolor.anvil"))return;
        if (event.getResult() == null)return;
        if (!event.getResult().hasItemMeta())return;
        ItemMeta resultMeta = event.getResult().getItemMeta();
        resultMeta.setDisplayName(Players.addColor(event.getInventory().getRenameText()));
        event.getResult().setItemMeta(resultMeta);
    }
}
