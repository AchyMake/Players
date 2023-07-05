package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Warps;
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
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /delwarp warpName");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (getWarps().locationExist(args[0])) {
                    getWarps().delWarp(args[0]);
                    Players.send(player, args[0] + "&6 has been deleted");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Players.send(commandSender, "Usage: /delwarp warpName");
            }
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                if (getWarps().locationExist(args[0])) {
                    getWarps().delWarp(args[0]);
                    Players.send(commandSender, args[0] + " has been deleted");
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