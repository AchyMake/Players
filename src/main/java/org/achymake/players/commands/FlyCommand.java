package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public FlyCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.setAllowFlight(!player.getAllowFlight());
                if (player.getAllowFlight()) {
                    getMessage().sendActionBar(player, "&6&lFly:&a Enabled");
                } else {
                    getMessage().sendActionBar(player, "&6&lFly:&c Disabled");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.fly.others")) {
                    Player target = getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        target.setAllowFlight(!target.getAllowFlight());
                        if (target.getAllowFlight()) {
                            getMessage().sendActionBar(target, "&6&lFly:&a Enabled");
                        } else {
                            getMessage().sendActionBar(target, "&6&lFly:&c Disabled");
                        }
                    } else {
                        if (target != null) {
                            if (!target.hasPermission("players.command.fly.exempt")) {
                                target.setAllowFlight(!target.getAllowFlight());
                                if (target.getAllowFlight()) {
                                    getMessage().sendActionBar(target, "&6&lFly:&a Enabled");
                                    getMessage().send(player, "&6You enabled fly for&f " + target.getName());
                                } else {
                                    getMessage().sendActionBar(target, "&6&lFly:&c Disabled");
                                    getMessage().send(player, "&6You disabled fly for&f " + target.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setAllowFlight(!target.getAllowFlight());
                    if (target.getAllowFlight()) {
                        getMessage().sendActionBar(target, "&6&lFly:&a Enabled");
                        getMessage().send(consoleCommandSender, "You enabled fly for " + target.getName());
                    } else {
                        getMessage().sendActionBar(target, "&6&lFly:&c Disabled");
                        getMessage().send(consoleCommandSender, "You disabled fly for " + target.getName());
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
                if (player.hasPermission("players.command.fly.others")) {
                    for (Player players : getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.fly.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
