package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrepareAnvil implements Listener {
    private Message getMessage() {
        return Players.getMessage();
    }
    public PrepareAnvil(Players players) {
        players.getServer().getPluginManager().registerEvents(this, players);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (!event.getView().getPlayer().hasPermission("players.chatcolor.anvil"))return;
        ItemStack itemStack = event.getResult();
        if (itemStack == null)return;
        if (!itemStack.hasItemMeta())return;
        ItemMeta resultMeta = itemStack.getItemMeta();
        resultMeta.setDisplayName(getMessage().addColor(event.getInventory().getRenameText()));
        itemStack.setItemMeta(resultMeta);
    }
}