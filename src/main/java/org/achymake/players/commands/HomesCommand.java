package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomesCommand implements CommandExecutor, TabCompleter {
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
                if (getDatabase().getHomes(player).isEmpty()) {
                    getMessage().send(player, "&cYou haven't set any homes yet");
                } else {
                    getMessage().send(player, "&6Homes:");
                    for (String listedHomes : getDatabase().getHomes(player)) {
                        getMessage().send(player, "- " + listedHomes);
                    }
                }
            }
            if (args.length == 3) {
                String arg0 = args[0];
                String target = args[1];
                String targetHome = args[2];
                if (arg0.equalsIgnoreCase("delete")) {
                    if (player.hasPermission("players.command.homes.delete")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
                        if (getDatabase().exist(offlinePlayer)) {
                            if (getDatabase().getHomes(offlinePlayer).contains(targetHome)) {
                                getDatabase().setString(offlinePlayer, "homes." + targetHome, null);
                                getMessage().send(player, "&6Deleted&f " + targetHome + "&6 of&f " + target);
                            } else {
                                getMessage().send(player, target + "&c doesn't have&f " + targetHome);
                            }
                        } else {
                            getMessage().send(player, target + "&c has never joined");
                        }
                    }
                }
                if (arg0.equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("players.command.homes.teleport")) {
                        OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(target);
                        if (getDatabase().exist(offlinePlayer)) {
                            if (targetHome.equalsIgnoreCase("bed")) {
                                if (offlinePlayer.getBedSpawnLocation() != null) {
                                    player.teleport(offlinePlayer.getBedSpawnLocation());
                                    getMessage().send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                }
                            } else {
                                if (getDatabase().getHomes(offlinePlayer).contains(targetHome)) {
                                    getDatabase().getHome(offlinePlayer, targetHome).getChunk().load();
                                    player.teleport(getDatabase().getHome(offlinePlayer, targetHome));
                                    getMessage().send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                } else {
                                    getMessage().send(player, target + "&c doesn't have&f " + targetHome);
                                }
                            }
                        } else {
                            getMessage().send(player, target + "&c has never joined");
                        }
                    }
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.homes.delete")) {
                    commands.add("delete");
                }
                if (player.hasPermission("players.command.homes.teleport")) {
                    commands.add("teleport");
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("players.command.homes.teleport")) {
                        for (OfflinePlayer offlinePlayers : player.getServer().getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("players.command.homes.delete")) {
                        for (OfflinePlayer offlinePlayers : player.getServer().getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
            }
            if (args.length == 3) {
                if (player.hasPermission("players.command.homes.teleport")) {
                    OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(args[1]);
                    if (getDatabase().exist(offlinePlayer)) {
                        commands.addAll(getDatabase().getHomes(offlinePlayer));
                    }
                }
            }
        }
        return commands;
    }
}
