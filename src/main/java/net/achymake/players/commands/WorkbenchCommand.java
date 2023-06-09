package net.achymake.players.commands;

import net.achymake.players.Players;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                player.openWorkbench(player.getLocation(), true);
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.workbench.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        target.openWorkbench(target.getLocation(), true);
                    } else {
                        if (target != null) {
                            target.openWorkbench(target.getLocation(), true);
                            Players.send(target, player.getName() + "&6 opened crafting table for you");
                            Players.send(player, "&6You opened crafting table for " + target.getName());
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.openWorkbench(target.getLocation(), true);
                    Players.send(commandSender, "You opened crafting table for " + target.getName());
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
                if (sender.hasPermission("players.command.workbench.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}