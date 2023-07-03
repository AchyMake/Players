package net.achymake.players.commands;

import net.achymake.players.Players;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GMSCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                    Players.send(player, "&cYou are already in&f Survival&c mode");
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                    Players.send(player, "&6You changed gamemode to&f Survival");
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.gamemode.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        if (!target.getGameMode().equals(GameMode.SURVIVAL)) {
                            target.setGameMode(GameMode.SURVIVAL);
                            Players.send(target, player.getName() + "&6 has changed your gamemode to&f Survival");
                            Players.send(player, "&6You changed&f " + target.getName() + "&6 gamemode to&f Survival");
                        }
                    } else {
                        if (target != null) {
                            if (target.hasPermission("players.command.gamemode.exempt")) {
                                Players.send(player, "&cYou are not allowed to change gamemode of&f " + target.getName());
                            } else {
                                if (target.getGameMode().equals(GameMode.SURVIVAL)) {
                                    Players.send(player, target.getName() + "&c is already in&f Survival&c mode");
                                } else {
                                    target.setGameMode(GameMode.SURVIVAL);
                                    Players.send(target, player.getName() + "&6 has changed your gamemode to&f Survival");
                                    Players.send(player, "&6You changed&f " + target.getName() + "&6 gamemode to&f Survival");
                                }
                            }
                        }
                    }
                } else {
                    Players.send(player, "&cError:&7 You do not have the permissions to execute the command");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (!target.getGameMode().equals(GameMode.SURVIVAL)) {
                        target.setGameMode(GameMode.SURVIVAL);
                        Players.send(target, "&6Your gamemode has changed to&f Survival");
                        Players.send(commandSender, "You changed " + target.getName() + " gamemode to Survival");
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
                if (sender.hasPermission("players.command.gamemode.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.gamemode.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(sender.getName());
                }
            }
        }
        return commands;
    }
}