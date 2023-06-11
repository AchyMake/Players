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

public class PVPCommand implements CommandExecutor, TabCompleter {
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
                getDatabase().setBoolean((Player) sender, "settings.pvp", !getDatabase().isPVP((Player) sender));
                if (getDatabase().isPVP((Player) sender)) {
                    getMessage().send(sender, "&6You enabled pvp");
                } else {
                    getMessage().send(sender, "&6You disabled pvp");
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.pvp.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target == sender) {
                    getDatabase().setBoolean(target, "settings.pvp", !getDatabase().isPVP(target));
                    if (getDatabase().isPVP(target)) {
                        getMessage().send(sender, "&6You enabled pvp for your self");
                    } else {
                        getMessage().send(sender, "&6You disabled pvp for your self");
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.pvp.exempt")) {
                            getDatabase().setBoolean(target, "settings.pvp", !getDatabase().isPVP(target));
                            if (getDatabase().isPVP(target)) {
                                getMessage().send(target, sender.getName() + "&6 enabled pvp for you");
                                getMessage().send(sender, "&6You enabled pvp for&f " + target.getName());
                            } else {
                                getMessage().send(target, sender.getName() + "&6 disabled pvp for you");
                                getMessage().send(sender, "&6You disabled pvp for&f " + target.getName());
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (getDatabase().exist(offlinePlayer)) {
                            getDatabase().setBoolean(offlinePlayer, "settings.pvp", !getDatabase().isPVP(offlinePlayer));
                            if (getDatabase().isPVP(offlinePlayer)) {
                                getMessage().send(sender, "&6You enabled pvp for&f " + offlinePlayer.getName());
                            } else {
                                getMessage().send(sender, "&6You disabled pvp for&f " + offlinePlayer.getName());
                            }
                        } else {
                            getMessage().send(sender, offlinePlayer.getName() + "&c has never joined");
                        }
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
                Player player = (Player) sender;
                if (player.hasPermission("players.command.pvp.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.pvp.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(sender.getName());
                }
            }
        }
        return commands;
    }
}