package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (getDatabase().hasCooldown(player, "feed")) {
                    Players.sendActionBar(player, "&cYou have to wait&f " + getDatabase().getCooldown(player, "feed") + "&c seconds");
                } else {
                    player.setFoodLevel(20);
                    Players.sendActionBar(player, "&6Your starvation has been satisfied");
                    getDatabase().addCooldown(player, "feed");
                    Players.send(player, "&6You satisfied&f " + player.getName() + "&6's starvation");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.feed.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        target.setFoodLevel(20);
                        Players.sendActionBar(target, "&6Your starvation has been satisfied");
                        Players.send(player, "&6You satisfied&f " + target.getName() + "&6's starvation");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    Players.sendActionBar(target, "&6Your starvation has been satisfied");
                    Players.send(consoleCommandSender, "You satisfied " + target.getName() + "'s starvation");
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
                if (player.hasPermission("players.command.feed.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
