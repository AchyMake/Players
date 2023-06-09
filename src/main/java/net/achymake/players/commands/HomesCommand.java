package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
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
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (database.getHomes(player).size() > 0) {
                    message.send(player, "&6Homes:");
                    for (String listedHomes : database.getHomes(player)) {
                        message.send(player, "- " + listedHomes);
                    }
                } else {
                    message.send(player, "&cYou haven't set any homes yet");
                }
            }
            if (args.length == 3) {
                Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("players.command.homes.delete")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (database.exist(offlinePlayer)) {
                            if (database.getHomes(offlinePlayer).contains(args[2])) {
                                database.setString(offlinePlayer, "homes." + args[2], null);
                                message.send(player, "&6Deleted&f " + args[2] + "&6 of&f " + args[1]);
                            } else {
                                message.send(player, args[1] + "&c doesn't have&f " + args[2]);
                            }
                        } else {
                            message.send(player, args[1] + "&c has never joined");
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("players.command.homes.teleport")) {
                        OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(args[1]);
                        if (database.exist(offlinePlayer)) {
                            if (args[2].equalsIgnoreCase("bed")) {
                                if (offlinePlayer.getBedSpawnLocation() != null) {
                                    player.teleport(offlinePlayer.getBedSpawnLocation());
                                    message.send(player, "&6Teleporting&f " + args[2] + "&6 of&f " + args[1]);
                                }
                            } else {
                                if (database.getHomes(offlinePlayer).contains(args[2])) {
                                    database.getHome(offlinePlayer, args[2]).getChunk().load();
                                    player.teleport(database.getHome(offlinePlayer, args[2]));
                                    message.send(player, "&6Teleporting&f " + args[2] + "&6 of&f " + args[1]);
                                } else {
                                    message.send(player, args[1] + "&c doesn't have&f " + args[2]);
                                }
                            }
                        } else {
                            message.send(player, args[1] + "&c has never joined");
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("players.command.homes.delete")) {
                    commands.add("delete");
                }
                if (sender.hasPermission("players.command.homes.teleport")) {
                    commands.add("teleport");
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (sender.hasPermission("players.command.homes.teleport")) {
                        for (OfflinePlayer offlinePlayers : sender.getServer().getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (sender.hasPermission("players.command.homes.delete")) {
                        for (OfflinePlayer offlinePlayers : sender.getServer().getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
            }
            if (args.length == 3) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.homes.teleport")) {
                    OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(args[1]);
                    if (database.exist(offlinePlayer)) {
                        commands.addAll(database.getHomes(offlinePlayer));
                    }
                }
            }
        }
        return commands;
    }
}