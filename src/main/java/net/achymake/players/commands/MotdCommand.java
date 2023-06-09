package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Motd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MotdCommand implements CommandExecutor, TabCompleter {
    private final Motd motd = Players.getMotd();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            motd.sendMotd(sender, "message-of-the-day");
        }
        if (args.length == 1) {
            motd.sendMotd(sender, args[0]);
        }
        if (args.length == 2) {
            if (sender.hasPermission("players.command.motd.others")) {
                Player target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    motd.sendMotd(target, args[0]);
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
                commands.addAll(motd.getMotds());
            }
            if (args.length == 2) {
                if (sender.hasPermission("players.command.motd.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}