package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getDatabase().setVanish(player, !getDatabase().isVanished(player));
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.vanish.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getDatabase().setVanish(target, !getDatabase().isVanished(target));
                        } else {
                            if (!target.hasPermission("players.command.vanish.exempt")) {
                                getDatabase().setVanish(target, !getDatabase().isVanished(target));
                                if (getDatabase().isVanished(target)) {
                                    Players.send(target, player.getName() + "&6 made you vanish");
                                    Players.send(player, target.getName() + "&6 is now vanished");
                                } else {
                                    Players.send(target, player.getName() + "&6 made you no longer vanish");
                                    Players.send(player, target.getName() + "&6 is no longer vanished");
                                }
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            getDatabase().setVanish(offlinePlayer, !getDatabase().isVanished(offlinePlayer));
                            if (getDatabase().isVanished(offlinePlayer)) {
                                Players.send(player, offlinePlayer.getName() + "&6 is now vanished");
                            } else {
                                Players.send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                            }
                        } else {
                            Players.send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
            if (args.length == 2) {
                Player target = player.getServer().getPlayerExact(args[0]);
                boolean value = Boolean.valueOf(args[1]);
                if (value) {
                    if (target != null) {
                        if (!getDatabase().isVanished(target)) {
                            if (target == player) {
                                getDatabase().setVanish(target, true);
                            } else {
                                if (!target.hasPermission("players.command.vanish.exempt")) {
                                    getDatabase().setVanish(target, true);
                                    Players.send(target, player.getName() + "&6 made you vanish");
                                    Players.send(player, target.getName() + "&6 is now vanished");
                                }
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            if (!getDatabase().isVanished(offlinePlayer)) {
                                getDatabase().setVanish(offlinePlayer, true);
                                if (getDatabase().isVanished(offlinePlayer)) {
                                    Players.send(player, offlinePlayer.getName() + "&6 is now vanished");
                                } else {
                                    Players.send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                                }
                            }
                        } else {
                            Players.send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                } else {
                    if (target != null) {
                        if (getDatabase().isVanished(target)) {
                            if (target == player) {
                                getDatabase().setVanish(target, false);
                            } else {
                                if (!target.hasPermission("players.command.vanish.exempt")) {
                                    getDatabase().setVanish(target, false);
                                    Players.send(target, player.getName() + "&6 made you no longer vanish");
                                    Players.send(player, target.getName() + "&6 is no longer vanished");
                                }
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            if (getDatabase().isVanished(offlinePlayer)) {
                                getDatabase().setVanish(offlinePlayer, false);
                                if (getDatabase().isVanished(offlinePlayer)) {
                                    Players.send(player, offlinePlayer.getName() + "&6 is now vanished");
                                } else {
                                    Players.send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                                }
                            }
                        } else {
                            Players.send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getDatabase().setVanish(target, !getDatabase().isVanished(target));
                    if (getDatabase().isVanished(target)) {
                        Players.send(consoleCommandSender, target.getName() + " is now vanished");
                    } else {
                        Players.send(consoleCommandSender, target.getName() + " is no longer vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = consoleCommandSender.getServer().getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setVanish(offlinePlayer, !getDatabase().isVanished(offlinePlayer));
                        if (getDatabase().isVanished(offlinePlayer)) {
                            Players.send(consoleCommandSender, offlinePlayer.getName() + " is now vanished");
                        } else {
                            Players.send(consoleCommandSender, offlinePlayer.getName() + " is no longer vanished");
                        }
                    } else {
                        Players.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
            }
            if (args.length == 2) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                boolean value = Boolean.valueOf(args[1]);
                if (value) {
                    if (target != null) {
                        if (!getDatabase().isVanished(target)) {
                            getDatabase().setVanish(target, true);
                            Players.send(consoleCommandSender, target.getName() + " is now vanished");
                        }
                    } else {
                        OfflinePlayer offlinePlayer = consoleCommandSender.getServer().getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            if (!getDatabase().isVanished(offlinePlayer)) {
                                getDatabase().setVanish(offlinePlayer, true);
                                if (getDatabase().isVanished(offlinePlayer)) {
                                    Players.send(consoleCommandSender, offlinePlayer.getName() + " is now vanished");
                                } else {
                                    Players.send(consoleCommandSender, offlinePlayer.getName() + " is no longer vanished");
                                }
                            }
                        } else {
                            Players.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                        }
                    }
                } else {
                    if (target != null) {
                        if (getDatabase().isVanished(target)) {
                            getDatabase().setVanish(target, false);
                            Players.send(consoleCommandSender, target.getName() + " is no longer vanished");
                        }
                    } else {
                        OfflinePlayer offlinePlayer = consoleCommandSender.getServer().getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            if (getDatabase().isVanished(offlinePlayer)) {
                                getDatabase().setVanish(offlinePlayer, false);
                                if (getDatabase().isVanished(offlinePlayer)) {
                                    Players.send(consoleCommandSender, offlinePlayer.getName() + " is now vanished");
                                } else {
                                    Players.send(consoleCommandSender, offlinePlayer.getName() + " is no longer vanished");
                                }
                            }
                        } else {
                            Players.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                        }
                    }
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
                if (player.hasPermission("players.command.vanish.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.vanish.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(player.getName());
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.vanish.others")) {
                    commands.add("true");
                    commands.add("false");
                }
            }
        }
        return commands;
    }
}
