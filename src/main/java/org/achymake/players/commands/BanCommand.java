package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BanCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public BanCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer.isBanned()) {
                    getMessage().send(player, offlinePlayer.getName() + "&c is already banned");
                } else {
                    long days = 2;
                    BanList banList = getServer().getBanList(BanList.Type.PROFILE);
                    banList.addBan(offlinePlayer, "&6You have been banned with no reason&7 by: " + player.getName(), Duration.ofDays(days), null);
                    getMessage().send(player, "You have banned " + offlinePlayer.getName() + " for no reason");
                }
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer.isBanned()) {
                    getMessage().send(player, offlinePlayer.getName() + "&c is already banned");
                } else {
                    long days = Long.parseLong(args[1]);
                    BanList banList = getServer().getBanList(BanList.Type.PROFILE);
                    banList.addBan(offlinePlayer, "&6You have been banned with no reason&7 by: " + player.getName(), Duration.ofDays(days), null);
                    getMessage().send(player, "You have banned " + offlinePlayer.getName() + " for no reason");
                }
            }
            if (args.length > 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer.isBanned()) {
                    getMessage().send(player, offlinePlayer.getName() + "&c is already banned");
                } else {
                    long days = Long.parseLong(args[1]);
                    BanList banList = getServer().getBanList(BanList.Type.PROFILE);
                    banList.addBan(offlinePlayer, args(args) + "&7 by: " + player.getName(), Duration.ofDays(days), null);
                    getMessage().send(player, "You have banned " + offlinePlayer.getName() + " for " + args(args));
                }
            }
        }
        return true;
    }
    private String args(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String words : args) {
            stringBuilder.append(words);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().strip();
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                for (OfflinePlayer offlinePlayer : getServer().getBannedPlayers()) {
                    commands.add(offlinePlayer.getName());
                }
            }
            if (args.length == 2) {
                commands.add("griefing");
                commands.add("hacking");
                commands.add("advertising");
                commands.add("");
            }
        }
        return commands;
    }
}
