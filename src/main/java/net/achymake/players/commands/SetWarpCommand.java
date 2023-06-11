package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import net.achymake.players.files.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetWarpCommand implements CommandExecutor, TabCompleter {
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
                getMessage().send(sender, "&cUsage:&f /setwarp warpName");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (getWarps().warpExist(args[0])) {
                    getWarps().setWarp(args[0], player.getLocation());
                    getMessage().send(sender, args[0] + "&6 has been relocated");
                } else {
                    getWarps().setWarp(args[0], player.getLocation());
                    getMessage().send(sender, args[0] + "&6 has been set");
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