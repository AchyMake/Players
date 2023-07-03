package net.achymake.players.commands;

import net.achymake.players.Players;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class AnnouncementCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /announcement message");
            } else {
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    Players.send(players, "&6Server:&f " + announcement(args));
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Players.send(commandSender, "Usage: /announcement message");
            } else {
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    Players.send(players, "&6Server:&f " + announcement(args));
                }
            }
        }
        return true;
    }
    private String announcement(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String words : args) {
            stringBuilder.append(words);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().strip();
    }
    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.EMPTY_LIST;
    }
}