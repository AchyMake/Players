package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NicknameCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!getDatabase().getConfig(player).getString("display-name").equals(getDatabase().getConfig(player).getString("name"))) {
                    getDatabase().setString(player, "display-name", getDatabase().getConfig(player).getString("name"));
                    player.setDisplayName(getDatabase().getConfig(player).getString("name"));
                    getDatabase().resetTabList();
                    Players.send(player, "&6You reset your nickname");
                }
            }
            if (args.length == 1) {
                if (!getDatabase().getConfig(player).getString("display-name").equals(args[0])) {
                    getDatabase().setString(player, "display-name", args[0]);
                    player.setDisplayName(args[0]);
                    getDatabase().resetTabList();
                    Players.send(player, "&6You changed your nickname to&f " + args[0]);
                } else {
                    Players.send(player, "&cYou already have&f " + args[0] + "&c as nickname");
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.nickname.others")) {
                    Player target = player.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (!getDatabase().getConfig(target).getString("display-name").equals(args[0])) {
                            getDatabase().setString(target, "display-name", args[0]);
                            target.setDisplayName(args[0]);
                            getDatabase().resetTabList();
                            Players.send(player, "&6You changed " + target.getName() + " nickname to&f " + args[0]);
                        } else {
                            Players.send(player, target.getName() + "&c already have&f " + args[0] + "&c as nickname");
                        }
                    } else {
                        Players.send(player, args[1] + "&c is currently offline");
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
                for (Player players : player.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.nickname.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
