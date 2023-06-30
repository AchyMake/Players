package net.achymake.players.listeners;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {
    private Message getMessage() {
        return Players.getMessage();
    }
    public SignChange(Players plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length; i++) {
            if (!event.getLine(i).contains("&"))return;
            if (!event.getPlayer().hasPermission("players.chatcolor.sign"))return;
            event.setLine(i, getMessage().addColor(event.getLine(i)));
        }
    }
}