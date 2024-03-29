package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomesCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public HomesCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().getHomes(player).isEmpty()) {
                    getMessage().send(player, "&cYou haven't set any homes yet");
                } else {
                    getMessage().send(player, "&6Homes:");
                    for (String listedHomes : getUserdata().getHomes(player)) {
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
                        if (getUserdata().exist(offlinePlayer)) {
                            if (getUserdata().getHomes(offlinePlayer).contains(targetHome)) {
                                getUserdata().setString(offlinePlayer, "homes." + targetHome, null);
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
                        OfflinePlayer offlinePlayer = getServer().getOfflinePlayer(target);
                        if (getUserdata().exist(offlinePlayer)) {
                            if (targetHome.equalsIgnoreCase("bed")) {
                                if (offlinePlayer.getBedSpawnLocation() != null) {
                                    player.teleport(offlinePlayer.getBedSpawnLocation());
                                    getMessage().send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                } else {
                                    getMessage().send(player, target + "&c do not have a bed");
                                }
                            } else {
                                if (getUserdata().getHomes(offlinePlayer).contains(targetHome)) {
                                    getUserdata().getHome(offlinePlayer, targetHome).getChunk().load();
                                    getMessage().send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                    player.teleport(getUserdata().getHome(offlinePlayer, targetHome));
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
                        for (OfflinePlayer offlinePlayers : getServer().getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("players.command.homes.delete")) {
                        for (OfflinePlayer offlinePlayers : getServer().getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
            }
            if (args.length == 3) {
                if (player.hasPermission("players.command.homes.teleport")) {
                    OfflinePlayer offlinePlayer = getServer().getOfflinePlayer(args[1]);
                    if (getUserdata().exist(offlinePlayer)) {
                        commands.addAll(getUserdata().getHomes(offlinePlayer));
                    }
                }
            }
        }
        return commands;
    }
}
