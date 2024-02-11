package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WalkSpeedCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Server getServer() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public WalkSpeedCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                float value = Float.parseFloat(args[0]);
                player.setWalkSpeed(value);
                getMessage().send(player, "&6You're walk speed has changed to&f " + value);
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.walkspeed.others")) {
                    float value = Float.parseFloat(args[0]);
                    Player target = getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (target.hasPermission("players.command.walkspeed.exempt")) {
                            getMessage().send(player, "&6You are not allowed to change&f " + target.getName() + " &6walk speed");
                        } else {
                            target.setFlySpeed(value);
                            getMessage().send(player, "&6You changed&f " + target.getName() + " &6walk speed to&f " + value);
                        }
                    } else {
                        getMessage().send(player, args[1] + "&c is currently offline");
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
                commands.add("0.2");
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.walkspeed.others")) {
                    for (Player players : getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.walkspeed.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}