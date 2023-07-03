package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Jail;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JailCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Jail getJail() {
        return Players.getJail();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /jail target");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().jailExist()) {
                        if (target == player) {
                            if (getDatabase().isJailed(target)) {
                                getDatabase().getLocation(target, "jail").getChunk().load();
                                target.teleport(getDatabase().getLocation(target, "jail"));
                                getDatabase().setBoolean(target, "jailed", false);
                                Players.send(target, "&cYou got free by&f " + player.getName());
                                Players.send(player, "&6You freed&f " + target.getName());
                                getDatabase().setString(target, "locations.jail", null);
                            } else {
                                getJail().getJail().getChunk().load();
                                getDatabase().setLocation(target, "jail");
                                target.teleport(getJail().getJail());
                                getDatabase().setBoolean(target, "jailed", true);
                                Players.send(target, "&cYou got jailed by&f " + player.getName());
                                Players.send(player, "&6You jailed&f " + target.getName());
                            }
                        } else if (!target.hasPermission("players.command.jail.exempt")) {
                            if (getDatabase().isJailed(target)) {
                                getDatabase().getLocation(target, "jail").getChunk().load();
                                target.teleport(getDatabase().getLocation(target, "jail"));
                                getDatabase().setBoolean(target, "jailed", false);
                                Players.send(target, "&cYou got free by&f " + player.getName());
                                Players.send(player, "&6You freed&f " + target.getName());
                                getDatabase().setString(target, "locations.jail", null);
                            } else {
                                getJail().getJail().getChunk().load();
                                getDatabase().setLocation(target, "jail");
                                target.teleport(getJail().getJail());
                                getDatabase().setBoolean(target, "jailed", true);
                                Players.send(target, "&cYou got jailed by&f " + player.getName());
                                Players.send(player, "&6You jailed&f " + target.getName());
                            }
                        }
                    }
                } else {
                    Players.send(player, args[0] + "&c is currently offline");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Players.send(commandSender, "Usage: /jail target");
            }
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().jailExist()) {
                        if (getDatabase().isJailed(target)) {
                            getDatabase().getLocation(target, "jail").getChunk().load();
                            target.teleport(getDatabase().getLocation(target, "jail"));
                            getDatabase().setBoolean(target, "jailed", false);
                            Players.send(target, "&cYou got free by&f " + commandSender.getName());
                            Players.send(commandSender, "&6You freed&f " + target.getName());
                            getDatabase().setString(target, "locations.jail", null);
                        } else {
                            getJail().getJail().getChunk().load();
                            getDatabase().setLocation(target, "jail");
                            target.teleport(getJail().getJail());
                            getDatabase().setBoolean(target, "jailed", true);
                            Players.send(target, "&cYou got jailed by&f " + commandSender.getName());
                            Players.send(commandSender, "&6You jailed&f " + target.getName());
                        }
                    }
                } else {
                    Players.send(commandSender, args[0] + " is currently offline");
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
                    if (!players.hasPermission("players.command.jail.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(sender.getName());
            }
        }
        return commands;
    }
}