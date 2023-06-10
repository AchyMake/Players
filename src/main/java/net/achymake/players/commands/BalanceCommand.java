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
    private final Database database = Players.getDatabase();
    private final EconomyProvider economyProvider = Players.getEconomyProvider();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                message.send(player, "&6Balance:&a " + economyProvider.currencyNameSingular() + economyProvider.format(database.getEconomy(player)));
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.balance.others")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (database.exist(offlinePlayer)) {
                        message.send(player, offlinePlayer.getName() + " &6balance:&a " + economyProvider.currencyNameSingular() + economyProvider.format(database.getEconomy(offlinePlayer)));
                    } else {
                        message.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (database.exist(offlinePlayer)) {
                    message.send(sender, offlinePlayer.getName() + " &6balance:&a " + economyProvider.currencyNameSingular() + economyProvider.format(database.getEconomy(offlinePlayer)));
                } else {
                    message.send(sender, offlinePlayer.getName() + "&c has never joined");
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