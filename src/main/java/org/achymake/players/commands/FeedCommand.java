package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public FeedCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().hasCooldown(player, "feed")) {
                    getMessage().sendActionBar(player, "&cYou have to wait&f " + getUserdata().getCooldown(player, "feed") + "&c seconds");
                } else {
                    player.setFoodLevel(20);
                    getMessage().sendActionBar(player, "&6Your starvation has been satisfied");
                    getUserdata().addCooldown(player, "feed");
                    getMessage().send(player, "&6You satisfied&f " + player.getName() + "&6's starvation");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.feed.others")) {
                    Player target = getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        target.setFoodLevel(20);
                        getMessage().sendActionBar(target, "&6Your starvation has been satisfied");
                        getMessage().send(player, "&6You satisfied&f " + target.getName() + "&6's starvation");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    getMessage().sendActionBar(target, "&6Your starvation has been satisfied");
                    getMessage().send(consoleCommandSender, "You satisfied " + target.getName() + "'s starvation");
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
                if (player.hasPermission("players.command.feed.others")) {
                    for (Player players : getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
