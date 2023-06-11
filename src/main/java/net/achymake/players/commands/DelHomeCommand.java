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

public class DelHomeCommand implements CommandExecutor, TabCompleter {
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
                getMessage().send(sender, "&cUsage:&f /delhome homeName");
            }
            if (args.length == 1) {
                if (getDatabase().homeExist((Player) sender, args[0])) {
                    getDatabase().setString((Player) sender, "homes." + args[0], null);
                    getMessage().send(sender, args[0] + "&6 has been deleted");
                } else {
                    getMessage().send(sender, args[0] + "&c does not exist");
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