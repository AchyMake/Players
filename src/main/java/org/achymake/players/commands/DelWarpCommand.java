package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Warps;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelWarpCommand implements CommandExecutor, TabCompleter {
    private Warps getWarps() {
        return Players.getWarps();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                Players.send(player, "&cUsage:&f /delwarp warpName");
            }
            if (args.length == 1) {
                if (getWarps().locationExist(args[0])) {
                    getWarps().delWarp(args[0]);
                    Players.send(player, args[0] + "&6 has been deleted");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                Players.send(consoleCommandSender, "Usage: /delwarp warpName");
            }
            if (args.length == 1) {
                if (getWarps().locationExist(args[0])) {
                    getWarps().delWarp(args[0]);
                    Players.send(consoleCommandSender, args[0] + " has been deleted");
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
                commands.addAll(getWarps().getWarps());
            }
        }
        return commands;
    }
}
