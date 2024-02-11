package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.MessageFormat;
import java.util.UUID;

public record PlayerQuit(Players plugin) implements Listener {
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeTeleportTask(player);
        removeTPAonQuit(player);
        getUserdata().setLocation(player, "quit");
        if (getUserdata().isVanished(player)) {
            removeVanishTask(player);
            plugin.getVanished().remove(player);
            event.setQuitMessage(null);
        } else {
            if (getConfig().getBoolean("connection.quit.enable")) {
                event.setQuitMessage(quitMessage(player));
                playSound();
            } else {
                if (player.hasPermission("players.event.quit.message")) {
                    event.setQuitMessage(quitMessage(player));
                    playSound();
                } else {
                    event.setQuitMessage(null);
                    for (Player players : getServer().getOnlinePlayers()) {
                        if (players.hasPermission("players.event.quit.notify")) {
                            getMessage().send(players, player.getName() + "&7 left the Server");
                        }
                    }
                }
            }
            getUserdata().resetTabList();
        }
    }
    private String quitMessage(Player player) {
        return getMessage().addColor(MessageFormat.format(getConfig().getString("connection.quit.message"), player.getName()));
    }
    private void removeTeleportTask(Player player) {
        if (getUserdata().hasTaskID(player, "teleport")) {
            getScheduler().cancelTask(getUserdata().getTaskID(player, "teleport"));
            getUserdata().removeTaskID(player, "teleport");
        }
    }
    private void removeVanishTask(Player player) {
        if (getUserdata().hasTaskID(player, "vanish")) {
            getScheduler().cancelTask(getUserdata().getTaskID(player, "vanish"));
            getUserdata().removeTaskID(player, "vanish");
        }
    }
    private void playSound() {
        if (getConfig().getBoolean("connection.quit.sound.enable")) {
            String soundType = getConfig().getString("connection.quit.sound.type");
            float soundVolume = (float) getConfig().getDouble("connection.quit.sound.volume");
            float soundPitch = (float) getConfig().getDouble("connection.quit.sound.pitch");
            for (Player players : getServer().getOnlinePlayers()) {
                players.playSound(players, Sound.valueOf(soundType), soundVolume, soundPitch);
            }
        }
    }
    private void removeTPAonQuit(Player player) {
        if (getUserdata().getConfig(player).isString("tpa.from")) {
            String uuidString = getUserdata().getConfig(player).getString("tpa.from");
            UUID uuid = UUID.fromString(uuidString);
            OfflinePlayer target = getServer().getOfflinePlayer(uuid);
            getUserdata().setString(target, "tpa.sent", null);
            int taskID = getUserdata().getConfig(target).getInt("task.tpa");
            if (getScheduler().isQueued(taskID)) {
                getScheduler().cancelTask(taskID);
                getUserdata().setString(player, "tpa.from", null);
            }
            getUserdata().setString(target, "task.tpa", null);
        } else if (getUserdata().getConfig(player).isString("tpa.sent")) {
            String uuidString = getUserdata().getConfig(player).getString("tpa.sent");
            UUID uuid = UUID.fromString(uuidString);
            OfflinePlayer target = getServer().getOfflinePlayer(uuid);
            getUserdata().setString(target, "tpa.from", null);
            int taskID = getUserdata().getConfig(player).getInt("task.tpa");
            if (getScheduler().isQueued(taskID)) {
                getScheduler().cancelTask(taskID);
                getUserdata().setString(player, "task.tpa", null);
            }
            getUserdata().setString(player, "tpa.sent", null);
        }
    }
}
