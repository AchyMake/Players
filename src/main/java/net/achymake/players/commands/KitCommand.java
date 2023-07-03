package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Kits;
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
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&6Kits:");
                for (String kitNames : getKits().getKits()) {
                    if (player.hasPermission("players.command.kit." + kitNames)) {
                        Players.send(player, "- " + kitNames);
                    }
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.kit." + args[0])) {
                    for (String kitNames : getKits().getKits()) {
                        if (args[0].equals(kitNames)) {
                            getKits().giveKitWithCooldown(player, args[0]);
                        }
                    }
                }
            }
            if (args.length == 2) {
                Player player = (Player) sender;
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
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Players.send(commandSender, "Kits:");
                for (String kitNames : getKits().getKits()) {
                    Players.send(commandSender, "- " + kitNames);
                }
            }
            if (args.length == 2) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    getKits().giveKit(target, args[0]);
                    Players.send(target, "&6You received&f " + args[0] + "&6 kit");
                    Players.send(commandSender, "You dropped " + args[0] + " kit to " + target.getName());
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
                for (String kitName : getKits().getKits()) {
                    if (sender.hasPermission("players.command.kit." + kitName)) {
                        commands.add(kitName);
                    }
                }
            }
            if (args.length == 2) {
                if (sender.hasPermission("players.command.kit.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}