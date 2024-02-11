package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.achymake.players.data.Warps;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Warps getWarps() {
        return plugin.getWarps();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public WarpCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
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
                if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                    return false;
                } else {
                    if (player.hasPermission("players.command.warp." + args[0])) {
                        if (getWarps().locationExist(args[0])) {
                            getUserdata().teleport(player, args[0], getWarps().getLocation(args[0]));
                        } else {
                            getMessage().send(player, args[0] + "&c does not exist");
                        }
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.warp.others")) {
                    Player target = getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (getUserdata().isFrozen(target) || getUserdata().isJailed(target)) {
                            return false;
                        } else {
                            if (getWarps().locationExist(args[0])) {
                                getUserdata().teleport(target, args[0], getWarps().getLocation(args[0]));
                            } else {
                                getMessage().send(player, args[0] + "&c does not exist");
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                Player target = getServer().getPlayerExact(args[1]);
                if (target != null) {
                    if (getUserdata().isFrozen(target) || getUserdata().isJailed(target)) {
                        return false;
                    } else {
                        if (getWarps().locationExist(args[0])) {
                            getUserdata().teleport(target, args[0], getWarps().getLocation(args[0]));
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
                    for (Player players : getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
