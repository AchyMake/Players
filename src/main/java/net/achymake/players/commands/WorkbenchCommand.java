package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchCommand implements CommandExecutor, TabCompleter {
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                player.openWorkbench(player.getLocation(), true);
            }
            if (args.length == 1) {
                if (sender.hasPermission("players.command.workbench.others")) {
                    Player target = sender.getServer().getPlayerExact(args[0]);
                    if (target == sender) {
                        target.openWorkbench(target.getLocation(), true);
                    } else {
                        if (target != null) {
                            target.openWorkbench(target.getLocation(), true);
                            getMessage().send(target, sender.getName() + "&6 opened crafting table for you");
                            getMessage().send(sender, "&6You opened crafting table for " + target.getName());
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.openWorkbench(target.getLocation(), true);
                    getMessage().send(sender, "You opened crafting table for " + target.getName());
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
                if (sender.hasPermission("players.command.workbench.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}