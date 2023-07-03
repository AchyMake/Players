package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.api.EconomyProvider;
import net.achymake.players.files.Database;
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
    private EconomyProvider getEconomyProvider() {
        return Players.getEconomyProvider();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&6Balance:&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(getDatabase().getEconomy((Player) sender)));
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.balance.others")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        Players.send(player, offlinePlayer.getName() + " &6balance:&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(getDatabase().getEconomy(offlinePlayer)));
                    } else {
                        Players.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getDatabase().exist(offlinePlayer)) {
                    Players.send(commandSender, offlinePlayer.getName() + " balance: " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(getDatabase().getEconomy(offlinePlayer)));
                } else {
                    Players.send(commandSender, offlinePlayer.getName() + " has never joined");
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("players.command.balance.others")) {
                    for (OfflinePlayer offlinePlayer : sender.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayer.getName());
                    }
                }
            }
        }
        return commands;
    }
}