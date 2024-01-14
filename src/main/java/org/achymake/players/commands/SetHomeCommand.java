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

public class SetHomeCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (getDatabase().setHome(player, "home")) {
                    Players.send(player, "home&6 has been set");
                } else {
                    Players.send(player, "&cYou have reach your limit of&f " + getDatabase().getHomes(player).size() + "&c homes");
                }
            }
            if (args.length == 1) {
                String homeName = args[0];
                if (homeName.equalsIgnoreCase("bed")) {
                    Players.send(player, "&cYou can't set home for&f " + homeName);
                } else {
                    if (getDatabase().setHome(player, homeName)) {
                        Players.send(player, homeName + "&6 has been set");
                    } else {
                        Players.send(player, "&cYou have reach your limit of&f " + getDatabase().getHomes(player).size() + "&c homes");
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
            Player player = (Player) sender;
            if (args.length == 1) {
                commands.addAll(getDatabase().getHomes(player));
            }
        }
        return commands;
    }
}
