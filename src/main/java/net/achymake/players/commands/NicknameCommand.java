package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
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
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (!getDatabase().getConfig(player).getString("display-name").equals(getDatabase().getConfig(player).getString("name"))) {
                    getDatabase().setString(player, "display-name", getDatabase().getConfig(player).getString("name"));
                    player.setDisplayName(getDatabase().getConfig(player).getString("name"));
                    getDatabase().resetTabList();
                    getMessage().send(player, "&6You reset your nickname");
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (!getDatabase().getConfig(player).getString("display-name").equals(args[0])) {
                    getDatabase().setString(player, "display-name", args[0]);
                    player.setDisplayName(args[0]);
                    getDatabase().resetTabList();
                    getMessage().send(player, "&6You changed your nickname to&f " + args[0]);
                } else {
                    getMessage().send(player, "&cYou already have&f " + args[0] + "&c as nickname");
                }
            }
            if (args.length == 2) {
                if (sender.hasPermission("players.command.nickname.others")) {
                    Player target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (!getDatabase().getConfig(target).getString("display-name").equals(args[0])) {
                            getDatabase().setString(target, "display-name", args[0]);
                            target.setDisplayName(args[0]);
                            getDatabase().resetTabList();
                            getMessage().send(sender, "&6You changed " + target.getName() + " nickname to&f " + args[0]);
                        } else {
                            getMessage().send(sender, target.getName() + "&c already have&f " + args[0] + "&c as nickname");
                        }
                    } else {
                        getMessage().send(sender, args[1] + "&c is currently offline");
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
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
            if (args.length == 2) {
                if (sender.hasPermission("players.command.nickname.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}