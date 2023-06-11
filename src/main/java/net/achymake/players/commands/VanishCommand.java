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
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                getDatabase().setVanish(player, !getDatabase().isVanished(player));
                if (getDatabase().isVanished(player)) {
                    getMessage().send(player,"&6You are now vanished");
                } else {
                    getMessage().send(player, "&6You are no longer vanished");
                    getMessage().sendActionBar(player, "&6&lVanish:&c Disabled");
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.vanish.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getDatabase().setVanish(target, !getDatabase().isVanished(target));
                    if (getDatabase().isVanished(target)) {
                        getMessage().send(target, sender.getName() + "&6 made you vanish");
                        getMessage().send(sender, target.getName() + "&6 is now vanished");
                    } else {
                        getMessage().send(target, sender.getName() + "&6 made you no longer vanish");
                        getMessage().send(sender, target.getName() + "&6 is no longer vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setVanish(offlinePlayer, !getDatabase().isVanished(offlinePlayer));
                        if (getDatabase().isVanished(offlinePlayer)) {
                            getMessage().send(sender, offlinePlayer.getName() + "&6 is now vanished");
                        } else {
                            getMessage().send(sender, offlinePlayer.getName() + "&6 is no longer vanished");
                        }
                    } else {
                        getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (args.length == 2) {
            Player target = sender.getServer().getPlayerExact(args[0]);
            boolean value = Boolean.valueOf(args[1]);
            if (value) {
                if (target != null) {
                    if (!getDatabase().isVanished(target)) {
                        getDatabase().setVanish(target, true);
                        getMessage().send(target, sender.getName() + "&6 made you vanish");
                        getMessage().send(sender, target.getName() + "&6 is now vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        if (!getDatabase().isVanished(offlinePlayer)) {
                            getDatabase().setVanish(offlinePlayer, true);
                            if (getDatabase().isVanished(offlinePlayer)) {
                                getMessage().send(sender, offlinePlayer.getName() + "&6 is now vanished");
                            } else {
                                getMessage().send(sender, offlinePlayer.getName() + "&6 is no longer vanished");
                            }
                        }
                    } else {
                        getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            } else {
                if (target != null) {
                    if (getDatabase().isVanished(target)) {
                        getDatabase().setVanish(target, false);
                        getMessage().send(target, sender.getName() + "&6 made you no longer vanish");
                        getMessage().send(sender, target.getName() + "&6 is no longer vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        if (getDatabase().isVanished(offlinePlayer)) {
                            getDatabase().setVanish(offlinePlayer, false);
                            if (getDatabase().isVanished(offlinePlayer)) {
                                getMessage().send(sender, offlinePlayer.getName() + "&6 is now vanished");
                            } else {
                                getMessage().send(sender, offlinePlayer.getName() + "&6 is no longer vanished");
                            }
                        }
                    } else {
                        getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
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