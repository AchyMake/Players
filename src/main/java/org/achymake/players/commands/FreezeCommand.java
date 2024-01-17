package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FreezeCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&cUsage:&f /freeze target");
            }
            if (args.length == 1) {
                Player target = player.getServer().getPlayerExact(args[0]);
                if (target == player) {
                    getDatabase().setBoolean(target, "settings.frozen", !getDatabase().isFrozen(target));
                    if (getDatabase().isFrozen(target)) {
                        getMessage().send(player, "&6You froze&f " + target.getName());
                    } else {
                        getMessage().send(player, "&6You unfroze&f " + target.getName());
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.freeze.exempt")) {
                            getDatabase().setBoolean(target, "settings.frozen", !getDatabase().isFrozen(target));
                            if (getDatabase().isFrozen(target)) {
                                getMessage().send(player, "&6You froze&f " + target.getName());
                            } else {
                                getMessage().send(player, "&6You unfroze&f " + target.getName());
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            getDatabase().setBoolean(offlinePlayer, "settings.frozen", !getDatabase().isFrozen(offlinePlayer));
                            if (getDatabase().isFrozen(offlinePlayer)) {
                                getMessage().send(player, "&6You froze&f " + offlinePlayer.getName());
                            } else {
                                getMessage().send(player, "&6You unfroze&f " + offlinePlayer.getName());
                            }
                        } else {
                            getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().send(consoleCommandSender, "Usage: /freeze target");
            }
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getDatabase().setBoolean(target, "settings.frozen", !getDatabase().isFrozen(target));
                    if (getDatabase().isFrozen(target)) {
                        getMessage().send(consoleCommandSender, "You froze " + target.getName());
                    } else {
                        getMessage().send(consoleCommandSender, "You unfroze " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setBoolean(offlinePlayer, "settings.frozen", !getDatabase().isFrozen(offlinePlayer));
                        if (getDatabase().isFrozen(offlinePlayer)) {
                            getMessage().send(consoleCommandSender, "You froze " + offlinePlayer.getName());
                        } else {
                            getMessage().send(consoleCommandSender, "You unfroze " + offlinePlayer.getName());
                        }
                    } else {
                        getMessage().send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
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
                for (Player players : player.getServer().getOnlinePlayers()) {
                    if (!players.hasPermission("players.command.freeze.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(player.getName());
            }
        }
        return commands;
    }
}
