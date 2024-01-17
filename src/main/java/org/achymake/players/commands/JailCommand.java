package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Jail;
import org.achymake.players.files.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JailCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Jail getJail() {
        return getPlugin().getJail();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&cUsage:&f /jail target");
            }
            if (args.length == 1) {
                Player target = player.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().locationExist()) {
                        if (target == player) {
                            if (getDatabase().isJailed(target)) {
                                getDatabase().getLocation(target, "jail").getChunk().load();
                                target.teleport(getDatabase().getLocation(target, "jail"));
                                getDatabase().setBoolean(target, "jailed", false);
                                getMessage().send(target, "&cYou got free by&f " + player.getName());
                                getMessage().send(player, "&6You freed&f " + target.getName());
                                getDatabase().setString(target, "locations.jail", null);
                            } else {
                                getJail().getLocation().getChunk().load();
                                getDatabase().setLocation(target, "jail");
                                target.teleport(getJail().getLocation());
                                getDatabase().setBoolean(target, "jailed", true);
                                getMessage().send(target, "&cYou got jailed by&f " + player.getName());
                                getMessage().send(player, "&6You jailed&f " + target.getName());
                            }
                        } else if (!target.hasPermission("players.command.jail.exempt")) {
                            if (getDatabase().isJailed(target)) {
                                getDatabase().getLocation(target, "jail").getChunk().load();
                                target.teleport(getDatabase().getLocation(target, "jail"));
                                getDatabase().setBoolean(target, "jailed", false);
                                getMessage().send(target, "&cYou got free by&f " + player.getName());
                                getMessage().send(player, "&6You freed&f " + target.getName());
                                getDatabase().setString(target, "locations.jail", null);
                            } else {
                                getJail().getLocation().getChunk().load();
                                getDatabase().setLocation(target, "jail");
                                target.teleport(getJail().getLocation());
                                getDatabase().setBoolean(target, "jailed", true);
                                getMessage().send(target, "&cYou got jailed by&f " + player.getName());
                                getMessage().send(player, "&6You jailed&f " + target.getName());
                            }
                        }
                    }
                } else {
                    getMessage().send(player, args[0] + "&c is currently offline");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().send(consoleCommandSender, "Usage: /jail target");
            }
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().locationExist()) {
                        if (getDatabase().isJailed(target)) {
                            getDatabase().getLocation(target, "jail").getChunk().load();
                            target.teleport(getDatabase().getLocation(target, "jail"));
                            getDatabase().setBoolean(target, "jailed", false);
                            getMessage().send(target, "&cYou got free by&f " + consoleCommandSender.getName());
                            getMessage().send(consoleCommandSender, "&6You freed&f " + target.getName());
                            getDatabase().setString(target, "locations.jail", null);
                        } else {
                            getJail().getLocation().getChunk().load();
                            getDatabase().setLocation(target, "jail");
                            target.teleport(getJail().getLocation());
                            getDatabase().setBoolean(target, "jailed", true);
                            getMessage().send(target, "&cYou got jailed by&f " + consoleCommandSender.getName());
                            getMessage().send(consoleCommandSender, "&6You jailed&f " + target.getName());
                        }
                    }
                } else {
                    getMessage().send(consoleCommandSender, args[0] + " is currently offline");
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
                    if (!players.hasPermission("players.command.jail.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(player.getName());
            }
        }
        return commands;
    }
}
