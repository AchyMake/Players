package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public BalanceCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&6Balance:&a " + getEconomy().currency() + getEconomy().format(getEconomy().get(player)));
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.balance.others")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getUserdata().exist(offlinePlayer)) {
                        getMessage().send(player, offlinePlayer.getName() + "&6's balance:&a " + getEconomy().currency() + getEconomy().format(getEconomy().get(offlinePlayer)));
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getUserdata().exist(offlinePlayer)) {
                    getMessage().send(consoleCommandSender, offlinePlayer.getName() + "'s " + getEconomy().currency() + getEconomy().format(getEconomy().get(offlinePlayer)));
                } else {
                    getMessage().send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
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
                    for (OfflinePlayer offlinePlayer : getServer().getOfflinePlayers()) {
                        if (getUserdata().exist(offlinePlayer)) {
                            commands.add(offlinePlayer.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
