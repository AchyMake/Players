package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                database.setVanish(player, !database.isVanished(player));
                if (database.isVanished(player)) {
                    message.send(player,"&6You are now vanished");
                } else {
                    message.send(player, "&6You are no longer vanished");
                    message.sendActionBar(player, "&6&lVanish:&c Disabled");
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.vanish.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    database.setVanish(target, !database.isVanished(target));
                    if (database.isVanished(target)) {
                        message.send(target, sender.getName() + "&6 made you vanish");
                        message.send(sender, target.getName() + "&6 is now vanished");
                    } else {
                        message.send(target, sender.getName() + "&6 made you no longer vanish");
                        message.send(sender, target.getName() + "&6 is no longer vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (database.exist(offlinePlayer)) {
                        database.setVanish(offlinePlayer, !database.isVanished(offlinePlayer));
                        if (database.isVanished(offlinePlayer)) {
                            message.send(sender, offlinePlayer.getName() + "&6 is now vanished");
                        } else {
                            message.send(sender, offlinePlayer.getName() + "&6 is no longer vanished");
                        }
                    } else {
                        message.send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (args.length == 2) {
            Player target = sender.getServer().getPlayerExact(args[0]);
            boolean value = Boolean.valueOf(args[1]);
            if (value) {
                if (target != null) {
                    if (!database.isVanished(target)) {
                        database.setVanish(target, true);
                        message.send(target, sender.getName() + "&6 made you vanish");
                        message.send(sender, target.getName() + "&6 is now vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (database.exist(offlinePlayer)) {
                        if (!database.isVanished(offlinePlayer)) {
                            database.setVanish(offlinePlayer, true);
                            if (database.isVanished(offlinePlayer)) {
                                message.send(sender, offlinePlayer.getName() + "&6 is now vanished");
                            } else {
                                message.send(sender, offlinePlayer.getName() + "&6 is no longer vanished");
                            }
                        }
                    } else {
                        message.send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            } else {
                if (target != null) {
                    if (database.isVanished(target)) {
                        database.setVanish(target, false);
                        message.send(target, sender.getName() + "&6 made you no longer vanish");
                        message.send(sender, target.getName() + "&6 is no longer vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (database.exist(offlinePlayer)) {
                        if (database.isVanished(offlinePlayer)) {
                            database.setVanish(offlinePlayer, false);
                            if (database.isVanished(offlinePlayer)) {
                                message.send(sender, offlinePlayer.getName() + "&6 is now vanished");
                            } else {
                                message.send(sender, offlinePlayer.getName() + "&6 is no longer vanished");
                            }
                        }
                    } else {
                        message.send(sender, offlinePlayer.getName() + "&c has never joined");
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
                if (sender.hasPermission("players.command.vanish.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.vanish.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(sender.getName());
                }
            }
            if (args.length == 2) {
                if (sender.hasPermission("players.command.vanish.others")) {
                    commands.add("true");
                    commands.add("false");
                }
            }
        }
        return commands;
    }
}