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
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (database.homeExist(player, "home")) {
                    database.setHome(player, "home");
                    message.send(player, "home&6 has been set");
                } else if (database.getConfig(player).getInt("max-homes") > database.getHomes(player).size()) {
                    database.setHome(player, "home");
                    message.send(player, "home&6 has been set");
                } else {
                    message.send(player, "&cYou have reach your limit of&f " + database.getHomes(player).size() + "&c homes");
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("buy")) {
                    message.send(player, "&cYou can't set home for&f " + args[0]);
                } else if (args[0].equalsIgnoreCase("bed")) {
                    message.send(player, "&cYou can't set home for&f " + args[0]);
                } else {
                    if (database.homeExist(player, args[0])) {
                        database.setHome(player, args[0]);
                        message.send(player, args[0] + "&6 has been set");
                    } else if (database.getConfig(player).getInt("max-homes") > database.getHomes(player).size()) {
                        database.setHome(player, args[0]);
                        message.send(player, args[0] + "&6 has been set");
                    } else {
                        message.send(player, "&cYou have reach your limit of&f " + database.getHomes(player).size() + "&c homes");
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
                commands.addAll(database.getHomes((Player) sender));
            }
        }
        return commands;
    }
}