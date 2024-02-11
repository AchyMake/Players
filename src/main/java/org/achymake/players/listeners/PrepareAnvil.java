package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;

public record PrepareAnvil(Players plugin) implements Listener {
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (event.getResult() == null)return;
        if (!event.getResult().hasItemMeta())return;
        if (!event.getView().getPlayer().hasPermission("players.event.anvil.color"))return;
        ItemMeta resultMeta = event.getResult().getItemMeta();
        String renameText = event.getInventory().getRenameText();
        resultMeta.setDisplayName(getMessage().addColor(renameText));
        event.getResult().setItemMeta(resultMeta);
    }
}
