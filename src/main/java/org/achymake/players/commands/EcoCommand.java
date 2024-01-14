package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EcoCommand implements CommandExecutor, TabCompleter {
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                Players.send(player, "&cUsage:&f /eco add target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().resetEconomy(offlinePlayer);
                        Players.send(player, "&6You reset&f " + offlinePlayer.getName() + "&6 account to&a " + getDatabase().getEconomyFormat(getConfig().getDouble("economy.starting-balance")));
                    } else {
                        Players.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                double value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().addEconomy(offlinePlayer, value);
                        Players.send(player, "&6You added&a " + getDatabase().getEconomyFormat(value) + "&6 to&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        Players.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().removeEconomy(offlinePlayer, value);
                        Players.send(player, "&6You removed&a " + getDatabase().getEconomyFormat(value) + "&6 from&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        Players.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setEconomy(offlinePlayer, value);
                        Players.send(player, "&6You set&a " + getDatabase().getEconomyFormat(value) + "&6 to&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        Players.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;
            if (args.length == 0) {
                Players.send(consoleCommandSender, "Usage: /eco add target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().resetEconomy(offlinePlayer);
                        Players.send(consoleCommandSender, "You reset " + offlinePlayer.getName() + " account to " + getDatabase().getEconomyFormat(getConfig().getDouble("economy.starting-balance")));
                    } else {
                        Players.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                double value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().addEconomy(offlinePlayer, value);
                        Players.send(consoleCommandSender, "You added " + getDatabase().getEconomyFormat(value) + " to " + offlinePlayer.getName() + "&6 account");
                    } else {
                        Players.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().removeEconomy(offlinePlayer, value);
                        Players.send(consoleCommandSender, "&6You removed&a " + getDatabase().getEconomyFormat(value) + "&6 from&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        Players.send(consoleCommandSender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setEconomy(offlinePlayer, value);
                        Players.send(consoleCommandSender, "&6You set&a " + getDatabase().getEconomyFormat(value) + "&6 to&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        Players.send(consoleCommandSender, offlinePlayer.getName() + "&c has never joined");
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
            Player player = (Player) sender;
            if (args.length == 1) {
                commands.add("add");
                commands.add("remove");
                commands.add("reset");
                commands.add("set");
            }
            if (args.length == 2) {
                for (OfflinePlayer players : player.getServer().getOfflinePlayers()) {
                    commands.add(players.getName());
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
