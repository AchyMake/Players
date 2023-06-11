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

public class FlyCommand implements CommandExecutor, TabCompleter {
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setAllowFlight(!player.getAllowFlight());
                if (player.getAllowFlight()) {
                    getMessage().sendActionBar((Player) sender, "&6Enabled fly");
                } else {
                    getMessage().sendActionBar((Player) sender, "&6Disabled fly");
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.fly.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target == sender) {
                    target.setAllowFlight(!target.getAllowFlight());
                    if (target.getAllowFlight()) {
                        getMessage().sendActionBar((Player) sender, "&6Enabled fly");
                    } else {
                        getMessage().sendActionBar((Player) sender, "&6Disabled fly");
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.fly.exempt")) {
                            target.setAllowFlight(!target.getAllowFlight());
                            if (target.getAllowFlight()) {
                                getMessage().sendActionBar(target, "&6Enabled fly");
                                getMessage().send(sender, "&6You enabled fly for&f " + target.getName());
                            } else {
                                getMessage().sendActionBar(target, "&6Disabled fly");
                                getMessage().send(sender, "&6You disabled fly for&f " + target.getName());
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("players.command.fly.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.fly.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(sender.getName());
                }
            }
        }
        return commands;
    }
}