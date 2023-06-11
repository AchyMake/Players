package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InventoryCommand implements CommandExecutor, TabCompleter {
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                getMessage().send(player, "&cUsage:&f /inventory target");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                Player target = player.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (target != player) {
                        if (!target.hasPermission("players.command.inventory.exempt")) {
                            player.openInventory(target.getInventory());
                            getMessage().send(sender, "&6Opened inventory of " + target.getName());
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
            if (args.length == 1) {
                if (sender.hasPermission("players.command.inventory")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
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