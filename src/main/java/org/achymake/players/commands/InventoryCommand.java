package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InventoryCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                Players.send(player, "&cUsage:&f /inventory target");
            }
            if (args.length == 1) {
                Player target = player.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (target != player) {
                        if (!target.hasPermission("players.command.inventory.exempt")) {
                            player.openInventory(target.getInventory());
                            Players.send(player, "&6Opened inventory of " + target.getName());
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
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("players.command.inventory")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.inventory.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
