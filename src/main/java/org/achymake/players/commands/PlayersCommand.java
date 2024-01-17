package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&6" + getPlugin().getName() + " " + getPlugin().getDescription().getVersion());
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getPlugin().reload();
                    getMessage().send(player, "&6Players:&f files reloaded");
                }
                if (args[0].equalsIgnoreCase("discord")) {
                    getMessage().send(player, "&9Developers Discord");
                    getMessage().send(player, "https://discord.com/invite/aMtQFeJKyB");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().send(consoleCommandSender, getPlugin().getName() + " " + getPlugin().getDescription().getVersion());
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getPlugin().reload();
                    getMessage().send(consoleCommandSender, "Players: config files reloaded");
                }
                if (args[0].equalsIgnoreCase("discord")) {
                    getMessage().send(consoleCommandSender, "Developers Discord");
                    getMessage().send(consoleCommandSender, "https://discord.com/invite/aMtQFeJKyB");
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
