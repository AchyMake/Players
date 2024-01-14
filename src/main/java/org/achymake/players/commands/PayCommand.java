package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.api.EconomyProvider;
import org.achymake.players.files.Database;
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
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0 || args.length == 1) {
                Players.send(player, "&cUsage:&f /pay target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getDatabase().exist(offlinePlayer)) {
                    if (getEconomyProvider().has(player, Double.parseDouble(args[1]))) {
                        getEconomyProvider().withdrawPlayer(player, Double.parseDouble(args[1]));
                        getEconomyProvider().depositPlayer(offlinePlayer, Double.parseDouble(args[1]));
                        Players.send(player, "&6You paid&f " + offlinePlayer.getName() + "&a " + getEconomyProvider().format(Double.parseDouble(args[1])));
                    } else {
                        Players.send(player, "&cYou don't have&a " + getEconomyProvider().format(Double.parseDouble(args[1])) + "&c to pay&f " + offlinePlayer.getName());
                    }
                } else {
                    Players.send(player, offlinePlayer.getName() + "&c has never joined");
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
                for (Player players : player.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}
