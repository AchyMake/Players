package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&6" + getPlugin().getName() + " " + getPlugin().getDescription().getVersion());
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("reload")) {
                    Players.reload();
                    Players.send(player, "&6Players:&f files reloaded");
                }
                if (args[0].equalsIgnoreCase("discord")) {
                    Players.send(player, "&9Developers Discord");
                    Players.send(player, "https://discord.com/invite/aMtQFeJKyB");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Players.send(commandSender, getPlugin().getName() + " " + getPlugin().getDescription().getVersion());
            }
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                if (args[0].equalsIgnoreCase("reload")) {
                    Players.reload();
                    Players.send(commandSender, "Players: config files reloaded");
                }
                if (args[0].equalsIgnoreCase("discord")) {
                    Players.send(commandSender, "Developers Discord");
                    Players.send(commandSender, "https://discord.com/invite/aMtQFeJKyB");
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
                commands.add("reload");
                commands.add("discord");
            }
        }
        return commands;
    }
}
