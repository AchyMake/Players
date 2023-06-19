package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import net.achymake.players.files.Warps;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelWarpCommand implements CommandExecutor, TabCompleter {
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
                getMessage().send(sender, "&cUsage:&f /delwarp warpName");
            }
            if (args.length == 1) {
                if (getWarps().warpExist(args[0])) {
                    getWarps().delWarp(args[0]);
                    getMessage().send(sender, args[0] + "&6 has been deleted");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                getMessage().send(sender, "Usage: /delwarp warpName");
            }
            if (args.length == 1) {
                if (getWarps().warpExist(args[0])) {
                    getWarps().delWarp(args[0]);
                    getMessage().send(sender, args[0] + " has been deleted");
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