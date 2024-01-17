package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Message;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
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
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.rules.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
    private void sendHelp(CommandSender sender) {
        if (sender instanceof Player player) {
            if (getConfig().isList("help")) {
                for (String message : getConfig().getStringList("help")) {
                    getMessage().send(player, message.replaceAll("%player%", player.getName()));
                }
            } else if (getConfig().isString("help")) {
                getMessage().send(player, getConfig().getString("help").replaceAll("%player%", player.getName()));
            }
        }
        if (sender instanceof ConsoleCommandSender commandSender) {
            if (getConfig().isList("help")) {
                for (String message : getConfig().getStringList("help")) {
                    getMessage().send(commandSender, message.replaceAll("%player%", commandSender.getName()));
                }
            } else if (getConfig().isString("help")) {
                getMessage().send(commandSender, getConfig().getString("help").replaceAll("%player%", commandSender.getName()));
            }
        }
    }
}
