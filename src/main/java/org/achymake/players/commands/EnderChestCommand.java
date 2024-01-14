package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EnderChestCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.openInventory(player.getEnderChest());
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.enderchest.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        player.openInventory(target.getEnderChest());
                    } else {
                        if (target != null) {
                            if (!target.hasPermission("players.command.enderchest.exempt")) {
                                player.openInventory(target.getEnderChest());
                                Players.send(player, "&6Opened enderchest of&f " + target.getName());
                            }
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
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.enderchest.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.enderchest.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
