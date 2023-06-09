package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import net.achymake.players.files.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Warps warps = Players.getWarps();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            message.send(sender, "&cUsage:&f /warp warpName");
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                if (database.isFrozen((Player) sender) || database.isJailed((Player) sender)) {
                    return false;
                } else {
                    Player player = (Player) sender;
                    if (player.hasPermission("players.command.warp." + args[0])) {
                        if (warps.warpExist(args[0])) {
                            warps.getWarp(args[0]).getChunk().load();
                            message.sendActionBar(player, "&6Teleporting to&f "+ args[0]);
                            player.teleport(warps.getWarp(args[0]));
                        }
                    }
                }
            }
        }
        if (args.length == 2) {
            if (sender.hasPermission("players.command.warp.others")) {
                if (sender.hasPermission("players.command.warp." + args[0])) {
                    Player target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (database.isFrozen(target) || database.isJailed(target)) {
                            return false;
                        } else {
                            if (warps.warpExist(args[0])) {
                                warps.getWarp(args[0]).getChunk().load();
                                message.send(target, "&6Teleporting to&f " + args[0]);
                                target.teleport(warps.getWarp(args[0]));
                            }
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
                for (String warps : warps.getWarps()) {
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