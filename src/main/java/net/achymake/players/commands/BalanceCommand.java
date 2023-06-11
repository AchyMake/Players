package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.api.EconomyProvider;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
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
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                getMessage().send(sender, "&6Balance:&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(getDatabase().getEconomy((Player) sender)));
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.balance.others")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getMessage().send(player, offlinePlayer.getName() + " &6balance:&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(getDatabase().getEconomy(offlinePlayer)));
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getDatabase().exist(offlinePlayer)) {
                    getMessage().send(sender, offlinePlayer.getName() + " &6balance:&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(getDatabase().getEconomy(offlinePlayer)));
                } else {
                    getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
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