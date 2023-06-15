package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import net.achymake.players.files.Warps;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Warps getWarps() {
        return Players.getWarps();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    return false;
                } else {
                    if (getWarps().getWarps().isEmpty()) {
                        getMessage().send(sender, "&cWarps is currently empty");
                    } else {
                        getMessage().send(sender, "&6Warps:");
                        for (String warps : getWarps().getWarps()) {
                            getMessage().send(sender, "- " + warps);
                        }
                    }
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    return false;
                } else {
                    if (getWarps().getWarps().isEmpty()) {
                        getMessage().send(sender, "&cWarps is currently empty");
                    } else {
                        getMessage().send(sender, "&6Warps:");
                        for (String warps : getWarps().getWarps()) {
                            getMessage().send(sender, "- " + warps);
                        }
                    }
                }
                if (player.hasPermission("players.command.warp." + args[0])) {
                    if (getWarps().warpExist(args[0])) {
                        getWarps().getWarp(args[0]).getChunk().load();
                        getMessage().sendActionBar(player, "&6Teleporting to&f "+ args[0]);
                        player.teleport(getWarps().getWarp(args[0]));
                    }
                }
            }
            if (args.length == 2) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.warp.others")) {
                    if (player.hasPermission("players.command.warp." + args[0])) {
                        Player target = player.getServer().getPlayerExact(args[1]);
                        if (target != null) {
                            if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                                return false;
                            } else {
                                if (getWarps().warpExist(args[0])) {
                                    getWarps().getWarp(args[0]).getChunk().load();
                                    getMessage().send(target, "&6Teleporting to&f " + args[0]);
                                    target.teleport(getWarps().getWarp(args[0]));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 2) {
                Player target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                        return false;
                    } else {
                        if (getWarps().warpExist(args[0])) {
                            getWarps().getWarp(args[0]).getChunk().load();
                            getMessage().send(target, "&6Teleporting to&f " + args[0]);
                            target.teleport(getWarps().getWarp(args[0]));
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                for (String warps : getWarps().getWarps()) {
                    if (player.hasPermission("players.command.warp." + warps)) {
                        commands.add(warps);
                    }
                }
            }
            if (args.length == 2) {
                Player player = (Player) sender;
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