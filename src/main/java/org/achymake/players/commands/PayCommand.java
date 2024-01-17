package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.api.EconomyProvider;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private EconomyProvider getEconomyProvider() {
        return getPlugin().getEconomyProvider();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0 || args.length == 1) {
                getMessage().send(player, "&cUsage:&f /pay target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                double value = Double.parseDouble(args[1]);
                if (value >= getConfig().getDouble("economy.minimum-payment")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        if (getEconomyProvider().has(player, Double.parseDouble(args[1]))) {
                            getEconomyProvider().withdrawPlayer(player, Double.parseDouble(args[1]));
                            getEconomyProvider().depositPlayer(offlinePlayer, Double.parseDouble(args[1]));
                            getMessage().send(player, "&6You paid&f " + offlinePlayer.getName() + "&a " + getEconomyProvider().format(Double.parseDouble(args[1])));
                        } else {
                            getMessage().send(player, "&cYou don't have&a " + getEconomyProvider().format(Double.parseDouble(args[1])) + "&c to pay&f " + offlinePlayer.getName());
                        }
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }else {
                    getMessage().send(player, "&cMinimum payment is " + getDatabase().getEconomyFormat(getConfig().getDouble("economy.minimum-payment")));
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
