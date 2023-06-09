package net.achymake.players.commands;

import net.achymake.players.Players;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GamemodeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /gamemode gamemodeName");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("adventure")) {
                    if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                        Players.send(player, "&cYou are already in&f Adventure&c mode");
                    } else {
                        player.setGameMode(GameMode.ADVENTURE);
                        Players.send(player, "&6You changed gamemode to&f adventure");
                    }
                }
                if (args[0].equalsIgnoreCase("creative")) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        Players.send(player, "&cYou are already in&f Creative&c mode");
                    } else {
                        player.setGameMode(GameMode.CREATIVE);
                        Players.send(player, "&6You changed gamemode to&f creative");
                    }
                }
                if (args[0].equalsIgnoreCase("survival")) {
                    if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                        Players.send(player, "&cYou are already in&f Survival&c mode");
                    } else {
                        player.setGameMode(GameMode.SURVIVAL);
                        Players.send(player, "&6You changed gamemode to&f survival");
                    }
                }
                if (args[0].equalsIgnoreCase("spectator")) {
                    if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                        Players.send(player, "&cYou are already in&f Spectator&c mode");
                    } else {
                        player.setGameMode(GameMode.SPECTATOR);
                        Players.send(player, "&6You changed gamemode to&f spectator");
                    }
                }
            }
            if (args.length == 2) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.gamemode.others")) {
                    Player target = player.getServer().getPlayerExact(args[1]);
                    if (target == sender) {
                        if (args[0].equalsIgnoreCase("adventure")) {
                            if (target.getGameMode().equals(GameMode.ADVENTURE)) {
                                Players.send(player, "&cYou are already in&f Adventure&c mode");
                            } else {
                                target.setGameMode(GameMode.ADVENTURE);
                                Players.send(player, "&6You changed gamemode to&f Adventure");
                            }
                        }
                        if (args[0].equalsIgnoreCase("creative")) {
                            if (target.getGameMode().equals(GameMode.CREATIVE)) {
                                Players.send(player, "&cYou are already in&f Creative&c mode");
                            } else {
                                target.setGameMode(GameMode.CREATIVE);
                                Players.send(player, "&6You changed gamemode to&f Creative");
                            }
                        }
                        if (args[0].equalsIgnoreCase("survival")) {
                            if (target.getGameMode().equals(GameMode.SURVIVAL)) {
                                Players.send(player, "&cYou are already in&f Survival&c mode");
                            } else {
                                target.setGameMode(GameMode.SURVIVAL);
                                Players.send(player, "&6You changed gamemode to&f Survival");
                            }
                        }
                        if (args[0].equalsIgnoreCase("spectator")) {
                            if (target.getGameMode().equals(GameMode.SPECTATOR)) {
                                Players.send(player, "&cYou are already in&f Spectator&c mode");
                            } else {
                                target.setGameMode(GameMode.SPECTATOR);
                                Players.send(player, "&6You changed gamemode to&f Spectator");
                            }
                        }
                    } else {
                        if (target != null) {
                            if (!target.hasPermission("players.command.gamemode.exempt")) {
                                if (args[0].equalsIgnoreCase("adventure")) {
                                    if (target.getGameMode().equals(GameMode.ADVENTURE)) {
                                        Players.send(player, target.getName() + "&c is already in&f Adventure&c mode");
                                    } else {
                                        target.setGameMode(GameMode.ADVENTURE);
                                        Players.send(target, player.getName() + "&6 has changed your gamemode to&f Adventure");
                                        Players.send(player, "&6You changed&f " + target.getName() + "&6's mode to&f Adventure");
                                    }
                                }
                                if (args[0].equalsIgnoreCase("creative")) {
                                    if (target.getGameMode().equals(GameMode.CREATIVE)) {
                                        Players.send(player, target.getName() + "&c is already in&f Creative&c mode");
                                    } else {
                                        target.setGameMode(GameMode.CREATIVE);
                                        Players.send(target, player.getName() + "&6 has changed your gamemode to&f Creative");
                                        Players.send(player, "&6You changed&f " + target.getName() + "&6's mode to&f Creative");
                                    }
                                }
                                if (args[0].equalsIgnoreCase("survival")) {
                                    if (!target.getGameMode().equals(GameMode.SURVIVAL)) {
                                        Players.send(player, target.getName() + "&c is already in&f Survival&c mode");
                                    } else {
                                        target.setGameMode(GameMode.SURVIVAL);
                                        Players.send(target, player.getName() + "&6 has changed your gamemode to&f Survival");
                                        Players.send(player, "&6You changed&f " + target.getName() + "&6's mode to&f Survival");
                                    }
                                }
                                if (args[0].equalsIgnoreCase("spectator")) {
                                    if (target.getGameMode().equals(GameMode.SPECTATOR)) {
                                        Players.send(player, target.getName() + "&c is already in&f Spectator&c mode");
                                    } else {
                                        target.setGameMode(GameMode.SPECTATOR);
                                        Players.send(target, player.getName() + "&6 has changed your gamemode to&f Spectator");
                                        Players.send(player, "&6You changed&f " + target.getName() + "&6's mode to&f Spectator");
                                    }
                                }
                            }
                        } else {
                            Players.send(player, args[1] + "&c is currently offline");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Players.send(commandSender, "Usage: /gamemode gamemodeName");
            }
            if (args.length == 2) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("adventure")) {
                        if (target.getGameMode().equals(GameMode.ADVENTURE)) {
                            Players.send(commandSender, target.getName() + " is already in Adventure mode");
                        } else {
                            target.setGameMode(GameMode.ADVENTURE);
                            Players.send(target, "&6Your gamemode has changed to&f Adventure");
                            Players.send(commandSender, "You changed " + target.getName() + "'s mode to Adventure");
                        }
                    }
                    if (args[0].equalsIgnoreCase("creative")) {
                        if (target.getGameMode().equals(GameMode.CREATIVE)) {
                            Players.send(commandSender, target.getName() + " is already in Creative mode");
                        } else {
                            target.setGameMode(GameMode.CREATIVE);
                            Players.send(target, "&6Your gamemode has changed to&f Creative");
                            Players.send(commandSender, "You changed " + target.getName() + "'s mode to Creative");
                        }
                    }
                    if (args[0].equalsIgnoreCase("survival")) {
                        if (target.getGameMode().equals(GameMode.SURVIVAL)) {
                            Players.send(commandSender, target.getName() + " is already in Survival mode");
                        } else {
                            target.setGameMode(GameMode.SURVIVAL);
                            Players.send(target, "&6Your gamemode has changed to&f Survival");
                            Players.send(commandSender, "You changed " + target.getName() + "'s mode to Survival");
                        }
                    }
                    if (args[0].equalsIgnoreCase("spectator")) {
                        if (target.getGameMode().equals(GameMode.SPECTATOR)) {
                            Players.send(commandSender, target.getName() + " is already in Spectator mode");
                        } else {
                            target.setGameMode(GameMode.SPECTATOR);
                            Players.send(target, "&6Your gamemode has changed to&f Spectator");
                            Players.send(commandSender, "You changed " + target.getName() + "'s mode to Spectator");
                        }
                    }
                } else {
                    Players.send(commandSender, args[1] + " is currently offline");
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
                if (sender.hasPermission("players.command.gamemode.adventure")) {
                    commands.add("adventure");
                }
                if (sender.hasPermission("players.command.gamemode.creative")) {
                    commands.add("creative");
                }
                if (sender.hasPermission("players.command.gamemode.survival")) {
                    commands.add("survival");
                }
                if (sender.hasPermission("players.command.gamemode.spectator")) {
                    commands.add("spectator");
                }
            }
            if (args.length == 2) {
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