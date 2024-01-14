package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandExecutor, TabCompleter {
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.rules.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    sendHelp(target);
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
                if (sender.hasPermission("players.command.rules.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
    private void sendHelp(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (getConfig().isList("help")) {
                for (String message : getConfig().getStringList("help")) {
                    Players.send(player, message.replaceAll("%player%", player.getName()));
                }
            } else if (getConfig().isString("help")) {
                Players.send(player, getConfig().getString("help").replaceAll("%player%", player.getName()));
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
            if (getConfig().isList("help")) {
                for (String message : getConfig().getStringList("help")) {
                    Players.send(commandSender, message.replaceAll("%player%", commandSender.getName()));
                }
            } else if (getConfig().isString("help")) {
                Players.send(commandSender, getConfig().getString("help").replaceAll("%player%", commandSender.getName()));
            }
        }
    }
}
