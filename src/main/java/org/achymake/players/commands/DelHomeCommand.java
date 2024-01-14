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

public class DelHomeCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                Players.send(player, "&cUsage:&f /delhome homeName");
            }
            if (args.length == 1) {
                if (getDatabase().homeExist(player, args[0])) {
                    getDatabase().setString(player, "homes." + args[0], null);
                    Players.send(player, args[0] + "&6 has been deleted");
                } else {
                    Players.send(player, args[0] + "&c does not exist");
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
                commands.addAll(getDatabase().getHomes(player));
            }
        }
        return commands;
    }
}
