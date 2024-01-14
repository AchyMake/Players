package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RulesCommand implements CommandExecutor, TabCompleter {
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendRules(sender);
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.rules.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    sendRules(target);
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
    private void sendRules(CommandSender sender) {
        if (sender instanceof Player player) {
            if (getConfig().isList("rules")) {
                for (String message : getConfig().getStringList("rules")) {
                    Players.send(player, message.replaceAll("%player%", player.getName()));
                }
            } else if (getConfig().isString("rules")) {
                Players.send(player, getConfig().getString("rules").replaceAll("%player%", player.getName()));
            }
        }
        if (sender instanceof ConsoleCommandSender commandSender) {
            if (getConfig().isList("rules")) {
                for (String message : getConfig().getStringList("rules")) {
                    Players.send(commandSender, message.replaceAll("%player%", commandSender.getName()));
                }
            } else if (getConfig().isString("rules")) {
                Players.send(commandSender, getConfig().getString("rules").replaceAll("%player%", commandSender.getName()));
            }
        }
    }
}
