package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PVPCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getDatabase().setBoolean(player, "settings.pvp", !getDatabase().isPVP(player));
                if (getDatabase().isPVP(player)) {
                    Players.sendActionBar(player, "&6&lPVP:&a Enabled");
                } else {
                    Players.sendActionBar(player, "&6&lPVP:&c Disabled");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.pvp.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        getDatabase().setBoolean(target, "settings.pvp", !getDatabase().isPVP(target));
                        if (getDatabase().isPVP(target)) {
                            Players.sendActionBar(target, "&6&lPVP:&a Enabled");
                        } else {
                            Players.sendActionBar(target, "&6&lPVP:&c Disabled");
                        }
                    } else {
                        if (target != null) {
                            if (!target.hasPermission("players.command.pvp.exempt")) {
                                getDatabase().setBoolean(target, "settings.pvp", !getDatabase().isPVP(target));
                                if (getDatabase().isPVP(target)) {
                                    Players.send(target, player.getName() + "&6 enabled pvp for you");
                                    Players.sendActionBar(target, "&6&lPVP:&a Enabled");
                                    Players.send(player, "&6You enabled pvp for&f " + target.getName());
                                } else {
                                    Players.send(target, player.getName() + "&6 disabled pvp for you");
                                    Players.sendActionBar(target, "&6&lPVP:&c Disabled");
                                    Players.send(player, "&6You disabled pvp for&f " + target.getName());
                                }
                            }
                        } else {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                            if (getDatabase().exist(offlinePlayer)) {
                                getDatabase().setBoolean(offlinePlayer, "settings.pvp", !getDatabase().isPVP(offlinePlayer));
                                if (getDatabase().isPVP(offlinePlayer)) {
                                    Players.send(player, "&6You enabled pvp for&f " + offlinePlayer.getName());
                                } else {
                                    Players.send(player, "&6You disabled pvp for&f " + offlinePlayer.getName());
                                }
                            } else {
                                Players.send(player, offlinePlayer.getName() + "&c has never joined");
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getDatabase().setBoolean(target, "settings.pvp", !getDatabase().isPVP(target));
                    if (getDatabase().isPVP(target)) {
                        Players.send(target, consoleCommandSender.getName() + "&6 enabled pvp for you");
                        Players.sendActionBar(target, "&6&lPVP:&a Enabled");
                        Players.send(consoleCommandSender, "You enabled pvp for " + target.getName());
                    } else {
                        Players.send(target, consoleCommandSender.getName() + "&6 disabled pvp for you");
                        Players.sendActionBar(target, "&6&lPVP:&c Disabled");
                        Players.send(consoleCommandSender, "You disabled pvp for " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setBoolean(offlinePlayer, "settings.pvp", !getDatabase().isPVP(offlinePlayer));
                        if (getDatabase().isPVP(offlinePlayer)) {
                            Players.send(consoleCommandSender, "You enabled pvp for " + offlinePlayer.getName());
                        } else {
                            Players.send(consoleCommandSender, "You disabled pvp for " + offlinePlayer.getName());
                        }
                    } else {
                        Players.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
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
                if (player.hasPermission("players.command.pvp.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.pvp.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(player.getName());
                }
            }
        }
        return commands;
    }
}
