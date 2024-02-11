package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Server getServer() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public WorkbenchCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.openWorkbench(player.getLocation(), true);
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.workbench.others")) {
                    Player target = getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        target.openWorkbench(target.getLocation(), true);
                    } else {
                        if (target != null) {
                            target.openWorkbench(target.getLocation(), true);
                            getMessage().send(target, player.getName() + "&6 opened crafting table for you");
                            getMessage().send(player, "&6You opened crafting table for " + target.getName());
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender commandSender) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.openWorkbench(target.getLocation(), true);
                    getMessage().send(commandSender, "You opened crafting table for " + target.getName());
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
                if (player.hasPermission("players.command.workbench.others")) {
                    for (Player players : getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
