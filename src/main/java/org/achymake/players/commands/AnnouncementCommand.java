package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class AnnouncementCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                Players.send(player, "&cUsage:&f /announcement message");
            } else {
                for (Player players : player.getServer().getOnlinePlayers()) {
                    Players.send(players, "&6Server:&f " + announcement(args));
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                Players.send(consoleCommandSender, "Usage: /announcement message");
            } else {
                for (Player players : consoleCommandSender.getServer().getOnlinePlayers()) {
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
