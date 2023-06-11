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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EcoCommand implements CommandExecutor, TabCompleter {
    private FileConfiguration getConfig() {
        return Players.getInstance().getConfig();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    private EconomyProvider getEconomyProvider() {
        return Players.getEconomyProvider();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            getMessage().send(sender, "&cUsage:&f /eco add target amount");
        }
        if (args.length == 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            if (args[0].equalsIgnoreCase("reset")) {
                if (sender.hasPermission("players.command.eco.reset")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().resetEconomy(offlinePlayer);
                        getMessage().send(sender, "&6You reset&f " + offlinePlayer.getName() + "&6 account to&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(getConfig().getDouble("economy.starting-balance")));
                    } else {
                        getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (args.length == 3) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            double value = Double.parseDouble(args[2]);
            if (args[0].equalsIgnoreCase("add")) {
                if (sender.hasPermission("players.command.eco.add")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().addEconomy(offlinePlayer, value);
                        getMessage().send(sender, "&6You added&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(value) + "&6 to&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (sender.hasPermission("players.command.eco.remove")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().removeEconomy(offlinePlayer, value);
                        getMessage().send(sender, "&6You removed&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(value) + "&6 from&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("players.command.eco.set")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setEconomy(offlinePlayer, value);
                        getMessage().send(sender, "&6You set&a " + getEconomyProvider().currencyNameSingular() + getEconomyProvider().format(value) + "&6 to&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
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
                if (sender.hasPermission("players.command.eco.add")) {
                    commands.add("add");
                }
                if (sender.hasPermission("players.command.eco.remove")) {
                    commands.add("remove");
                }
                if (sender.hasPermission("players.command.eco.reset")) {
                    commands.add("reset");
                }
                if (sender.hasPermission("players.command.eco.set")) {
                    commands.add("set");
                }
            }
            if (args.length == 2) {
                if (sender.hasPermission("players.command.eco.add") || sender.hasPermission("players.command.eco.remove") || sender.hasPermission("players.command.eco.reset") || sender.hasPermission("players.command.eco.set")) {
                    for (OfflinePlayer players : sender.getServer().getOfflinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set")) {
                    commands.add("100");
                    commands.add("500");
                    commands.add("1000");
                }
            }
        }
        return commands;
    }
}