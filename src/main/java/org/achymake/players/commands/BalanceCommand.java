package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                Players.send(player, getDatabase().getEconomyFormat(getDatabase().getEconomy(player)));
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.balance.others")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        Players.send(player, offlinePlayer.getName() + "&6's&f " + getDatabase().getEconomyFormat(getDatabase().getEconomy(offlinePlayer)));
                    } else {
                        Players.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getDatabase().exist(offlinePlayer)) {
                    Players.send(consoleCommandSender, offlinePlayer.getName() + "'s " + getDatabase().getEconomyFormat(getDatabase().getEconomy(offlinePlayer)));
                } else {
                    Players.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.balance.others")) {
                    for (OfflinePlayer offlinePlayer : player.getServer().getOfflinePlayers()) {
                        if (getDatabase().exist(offlinePlayer)) {
                            commands.add(offlinePlayer.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
