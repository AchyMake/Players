package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Kits;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    private Kits getKits() {
        return Players.getKits();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                Players.send(player, "&6Kits:");
                for (String kitNames : getKits().getKits()) {
                    if (player.hasPermission("players.command.kit." + kitNames)) {
                        Players.send(player, "- " + kitNames);
                    }
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.kit." + args[0])) {
                    for (String kitNames : getKits().getKits()) {
                        if (args[0].equals(kitNames)) {
                            getKits().giveKitWithCooldown(player, args[0]);
                        }
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.kit.others")) {
                    Player target = player.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        getKits().giveKit(target, args[0]);
                        Players.send(target, "&6You received&f " + args[0] + "&6 kit");
                        Players.send(player, "&6You dropped&f " + args[0] + "&6 kit to&f " + target.getName());
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                Players.send(consoleCommandSender, "Kits:");
                for (String kitNames : getKits().getKits()) {
                    Players.send(consoleCommandSender, "- " + kitNames);
                }
            }
            if (args.length == 2) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    getKits().giveKit(target, args[0]);
                    Players.send(target, "&6You received&f " + args[0] + "&6 kit");
                    Players.send(consoleCommandSender, "You dropped " + args[0] + " kit to " + target.getName());
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
                for (String kitName : getKits().getKits()) {
                    if (player.hasPermission("players.command.kit." + kitName)) {
                        commands.add(kitName);
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.kit.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
