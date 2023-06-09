package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.api.EconomyProvider;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final EconomyProvider economyProvider = Players.getEconomyProvider();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0 || args.length == 1) {
                message.send(sender, "&cUsage:&f /pay target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (database.exist(offlinePlayer)) {
                    if (economyProvider.has((Player) sender, Double.parseDouble(args[1]))) {
                        economyProvider.withdrawPlayer((Player) sender, Double.parseDouble(args[1]));
                        economyProvider.depositPlayer(offlinePlayer, Double.parseDouble(args[1]));
                        message.send(sender, "&6You paid&f " + offlinePlayer.getName() + "&a " + economyProvider.currencyNameSingular() + economyProvider.format(Double.parseDouble(args[1])));
                    } else {
                        message.send(sender, "&cYou don't have&a " + economyProvider.currencyNameSingular() + economyProvider.format(Double.parseDouble(args[1])) + "&c to pay&f " + offlinePlayer.getName());
                    }
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
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}