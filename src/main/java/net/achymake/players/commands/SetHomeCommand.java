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

public class SetHomeCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (getDatabase().homeExist(player, "home")) {
                    getDatabase().setHome(player, "home");
                    getMessage().send(player, "home&6 has been set");
                } else if (getDatabase().getConfig(player).getInt("max-homes") > getDatabase().getHomes(player).size()) {
                    getDatabase().setHome(player, "home");
                    getMessage().send(player, "home&6 has been set");
                } else {
                    getMessage().send(player, "&cYou have reach your limit of&f " + getDatabase().getHomes(player).size() + "&c homes");
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("buy")) {
                    getMessage().send(player, "&cYou can't set home for&f " + args[0]);
                } else if (args[0].equalsIgnoreCase("bed")) {
                    getMessage().send(player, "&cYou can't set home for&f " + args[0]);
                } else {
                    if (getDatabase().homeExist(player, args[0])) {
                        getDatabase().setHome(player, args[0]);
                        getMessage().send(player, args[0] + "&6 has been set");
                    } else if (Players.getDatabase().getConfig(player).getInt("max-homes") > getDatabase().getHomes(player).size()) {
                        Players.getDatabase().setHome(player, args[0]);
                        getMessage().send(player, args[0] + "&6 has been set");
                    } else {
                        getMessage().send(player, "&cYou have reach your limit of&f " + getDatabase().getHomes(player).size() + "&c homes");
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
                commands.addAll(getDatabase().getHomes((Player) sender));
            }
        }
        return commands;
    }
}