package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.setAllowFlight(!player.getAllowFlight());
                if (player.getAllowFlight()) {
                    Players.sendActionBar(player, "&6&lFly:&a Enabled");
                } else {
                    Players.sendActionBar(player, "&6&lFly:&c Disabled");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.fly.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        target.setAllowFlight(!target.getAllowFlight());
                        if (target.getAllowFlight()) {
                            Players.sendActionBar(target, "&6&lFly:&a Enabled");
                        } else {
                            Players.sendActionBar(target, "&6&lFly:&c Disabled");
                        }
                    } else {
                        if (target != null) {
                            if (!target.hasPermission("players.command.fly.exempt")) {
                                target.setAllowFlight(!target.getAllowFlight());
                                if (target.getAllowFlight()) {
                                    Players.sendActionBar(target, "&6&lFly:&a Enabled");
                                    Players.send(player, "&6You enabled fly for&f " + target.getName());
                                } else {
                                    Players.sendActionBar(target, "&6&lFly:&c Disabled");
                                    Players.send(player, "&6You disabled fly for&f " + target.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setAllowFlight(!target.getAllowFlight());
                    if (target.getAllowFlight()) {
                        Players.sendActionBar(target, "&6&lFly:&a Enabled");
                        Players.send(consoleCommandSender, "You enabled fly for " + target.getName());
                    } else {
                        Players.sendActionBar(target, "&6&lFly:&c Disabled");
                        Players.send(consoleCommandSender, "You disabled fly for " + target.getName());
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
                if (player.hasPermission("players.command.fly.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.fly.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
