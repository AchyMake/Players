package net.achymake.players.commands;

import net.achymake.players.Players;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPHereCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /tphere target");
            }
            if (args.length == 1) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                Player player = (Player) sender;
                if (target != null) {
                    Players.sendActionBar(target, "&6Teleporting to&f " + player.getName());
                    Players.sendActionBar(player, "&6Teleporting&f " + target.getName() + "&6 to you");
                    target.teleport(player.getLocation());
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
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}