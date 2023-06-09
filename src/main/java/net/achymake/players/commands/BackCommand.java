package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                return false;
            } else {
                if (args.length == 0) {
                    getDatabase().teleportBack(player);
                }
                if (args.length == 1) {
                    if (player.hasPermission("players.command.back.others")) {
                        Player target = player.getServer().getPlayerExact(args[0]);
                        if (target != null) {
                            if (target == player) {
                                getDatabase().teleportBack(target);
                            } else {
                                if (!target.hasPermission("players.command.back.exempt")) {
                                    getDatabase().teleportBack(target);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                    return false;
                } else {
                    if (target != null) {
                        getDatabase().teleportBack(target);
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
                if (sender.hasPermission("players.command.back.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.back.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}