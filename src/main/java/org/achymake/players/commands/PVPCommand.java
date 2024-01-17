package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PVPCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getDatabase().setBoolean(player, "settings.pvp", !getDatabase().isPVP(player));
                if (getDatabase().isPVP(player)) {
                    getMessage().sendActionBar(player, "&6&lPVP:&a Enabled");
                } else {
                    getMessage().sendActionBar(player, "&6&lPVP:&c Disabled");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.pvp.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        getDatabase().setBoolean(target, "settings.pvp", !getDatabase().isPVP(target));
                        if (getDatabase().isPVP(target)) {
                            getMessage().sendActionBar(target, "&6&lPVP:&a Enabled");
                        } else {
                            getMessage().sendActionBar(target, "&6&lPVP:&c Disabled");
                        }
                    } else {
                        if (target != null) {
                            if (!target.hasPermission("players.command.pvp.exempt")) {
                                getDatabase().setBoolean(target, "settings.pvp", !getDatabase().isPVP(target));
                                if (getDatabase().isPVP(target)) {
                                    getMessage().send(target, player.getName() + "&6 enabled pvp for you");
                                    getMessage().sendActionBar(target, "&6&lPVP:&a Enabled");
                                    getMessage().send(player, "&6You enabled pvp for&f " + target.getName());
                                } else {
                                    getMessage().send(target, player.getName() + "&6 disabled pvp for you");
                                    getMessage().sendActionBar(target, "&6&lPVP:&c Disabled");
                                    getMessage().send(player, "&6You disabled pvp for&f " + target.getName());
                                }
                            }
                        } else {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                            if (getDatabase().exist(offlinePlayer)) {
                                getDatabase().setBoolean(offlinePlayer, "settings.pvp", !getDatabase().isPVP(offlinePlayer));
                                if (getDatabase().isPVP(offlinePlayer)) {
                                    getMessage().send(player, "&6You enabled pvp for&f " + offlinePlayer.getName());
                                } else {
                                    getMessage().send(player, "&6You disabled pvp for&f " + offlinePlayer.getName());
                                }
                            } else {
                                getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
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
                        getMessage().send(target, consoleCommandSender.getName() + "&6 enabled pvp for you");
                        getMessage().sendActionBar(target, "&6&lPVP:&a Enabled");
                        getMessage().send(consoleCommandSender, "You enabled pvp for " + target.getName());
                    } else {
                        getMessage().send(target, consoleCommandSender.getName() + "&6 disabled pvp for you");
                        getMessage().sendActionBar(target, "&6&lPVP:&c Disabled");
                        getMessage().send(consoleCommandSender, "You disabled pvp for " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setBoolean(offlinePlayer, "settings.pvp", !getDatabase().isPVP(offlinePlayer));
                        if (getDatabase().isPVP(offlinePlayer)) {
                            getMessage().send(consoleCommandSender, "You enabled pvp for " + offlinePlayer.getName());
                        } else {
                            getMessage().send(consoleCommandSender, "You disabled pvp for " + offlinePlayer.getName());
                        }
                    } else {
                        getMessage().send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
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
