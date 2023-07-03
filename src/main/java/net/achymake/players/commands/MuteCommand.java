package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MuteCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /mute target");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target == player) {
                    getDatabase().setBoolean(target, "settings.muted", !getDatabase().isMuted(target));
                    if (getDatabase().isMuted(target)) {
                        Players.send(player, "&6You muted your self");
                    } else {
                        Players.send(player, "&6You unmuted your self");
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.mute.exempt")) {
                            getDatabase().setBoolean(target, "settings.muted", !getDatabase().isMuted(target));
                            if (getDatabase().isMuted(target)) {
                                Players.send(player, "&6You muted&f " + target.getName());
                            } else {
                                Players.send(player, "&6You unmuted&f " + target.getName());
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            getDatabase().setBoolean(offlinePlayer, "settings.muted", !getDatabase().isMuted(offlinePlayer));
                            if (getDatabase().isMuted(offlinePlayer)) {
                                Players.send(player, "&6You muted&f " + offlinePlayer.getName());
                            } else {
                                Players.send(player, "&6You unmuted&f " + offlinePlayer.getName());
                            }
                        } else {
                            Players.send(player,offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Players.send(commandSender, "Usage: /mute target");
            }
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getDatabase().setBoolean(target, "settings.muted", !getDatabase().isMuted(target));
                    if (getDatabase().isMuted(target)) {
                        Players.send(commandSender, "You muted " + target.getName());
                    } else {
                        Players.send(commandSender, "You unmuted " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setBoolean(offlinePlayer, "settings.muted", !getDatabase().isMuted(offlinePlayer));
                        if (getDatabase().isMuted(offlinePlayer)) {
                            Players.send(commandSender, "You muted " + offlinePlayer.getName());
                        } else {
                            Players.send(commandSender, "You unmuted " + offlinePlayer.getName());
                        }
                    } else {
                        Players.send(commandSender,offlinePlayer.getName() + " has never joined");
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
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    if (!players.hasPermission("players.command.mute.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(sender.getName());
            }
        }
        return commands;
    }
}