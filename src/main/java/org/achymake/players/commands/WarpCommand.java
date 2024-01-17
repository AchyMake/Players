package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.achymake.players.files.Warps;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Warps getWarps() {
        return getPlugin().getWarps();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    return false;
                } else {
                    if (getWarps().getWarps().isEmpty()) {
                        getMessage().send(player, "&cWarps is currently empty");
                    } else {
                        getMessage().send(player, "&6Warps:");
                        for (String warps : getWarps().getWarps()) {
                            getMessage().send(player, "- " + warps);
                        }
                    }
                }
            }
            if (args.length == 1) {
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    return false;
                } else {
                    if (player.hasPermission("players.command.warp." + args[0])) {
                        if (getWarps().locationExist(args[0])) {
                            getWarps().getLocation(args[0]).getChunk().load();
                            getMessage().sendActionBar(player, "&6Teleporting to&f "+ args[0]);
                            player.teleport(getWarps().getLocation(args[0]));
                        } else {
                            getMessage().send(player, args[0] + "&c does not exist");
                        }
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.warp.others")) {
                    if (player.hasPermission("players.command.warp." + args[0])) {
                        Player target = player.getServer().getPlayerExact(args[1]);
                        if (target != null) {
                            if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                                return false;
                            } else {
                                if (getWarps().locationExist(args[0])) {
                                    getWarps().getLocation(args[0]).getChunk().load();
                                    getMessage().send(target, "&6Teleporting to&f " + args[0]);
                                    target.teleport(getWarps().getLocation(args[0]));
                                } else {
                                    getMessage().send(player, args[0] + "&c does not exist");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                        return false;
                    } else {
                        if (getWarps().locationExist(args[0])) {
                            getWarps().getLocation(args[0]).getChunk().load();
                            getMessage().send(target, "&6Teleporting to&f " + args[0]);
                            target.teleport(getWarps().getLocation(args[0]));
                        } else {
                            getMessage().send(consoleCommandSender, args[0] + " does not exist");
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
                for (String warps : getWarps().getWarps()) {
                    if (player.hasPermission("players.command.warp." + warps)) {
                        commands.add(warps);
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.warp.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
