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
            if (args.length == 0 || args.length == 1) {
                Players.getMessage().send(sender, "&cUsage:&f /pay target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getDatabase().exist(offlinePlayer)) {
                    if (getEconomyProvider().has((Player) sender, Double.parseDouble(args[1]))) {
                        getEconomyProvider().withdrawPlayer((Player) sender, Double.parseDouble(args[1]));
                        getEconomyProvider().depositPlayer(offlinePlayer, Double.parseDouble(args[1]));
                        getMessage().send(sender, "&6You paid&f " + offlinePlayer.getName() + "&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(Double.parseDouble(args[1])));
                    } else {
                        getMessage().send(sender, "&cYou don't have&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(Double.parseDouble(args[1])) + "&c to pay&f " + offlinePlayer.getName());
                    }
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
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}