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

public class DelWarpCommand implements CommandExecutor, TabCompleter {
    private final Warps warps = Players.getWarps();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            message.send(sender, "&cUsage:&f /delwarp warpName");
        }
        if (args.length == 1) {
            if (warps.warpExist(args[0])) {
                warps.delWarp(args[0]);
                message.send(sender, args[0] + "&6 has been deleted");
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.addAll(warps.getWarps());
            }
        }
        return commands;
    }
}