package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Warps;
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
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                Players.send(player, "&cUsage:&f /setwarp warpName");
            }
            if (args.length == 1) {
                if (getWarps().locationExist(args[0])) {
                    getWarps().setLocation(args[0], player.getLocation());
                    Players.send(player, args[0] + "&6 has been relocated");
                } else {
                    getWarps().setLocation(args[0], player.getLocation());
                    Players.send(player, args[0] + "&6 has been set");
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
