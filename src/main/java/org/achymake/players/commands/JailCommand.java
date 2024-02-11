package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Jail;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JailCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Jail getJail() {
        return plugin.getJail();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public JailCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().locationExist()) {
                        if (target == player) {
                            if (getUserdata().isJailed(target)) {
                                getUserdata().getLocation(target, "jail").getChunk().load();
                                target.teleport(getUserdata().getLocation(target, "jail"));
                                getUserdata().setBoolean(target, "jailed", false);
                                getMessage().send(target, "&cYou got free by&f " + player.getName());
                                getMessage().send(player, "&6You freed&f " + target.getName());
                                getUserdata().setString(target, "locations.jail", null);
                            } else {
                                getJail().getLocation().getChunk().load();
                                getUserdata().setLocation(target, "jail");
                                target.teleport(getJail().getLocation());
                                getUserdata().setBoolean(target, "jailed", true);
                                getMessage().send(target, "&cYou got jailed by&f " + player.getName());
                                getMessage().send(player, "&6You jailed&f " + target.getName());
                            }
                        } else if (!target.hasPermission("players.command.jail.exempt")) {
                            if (getUserdata().isJailed(target)) {
                                getUserdata().getLocation(target, "jail").getChunk().load();
                                target.teleport(getUserdata().getLocation(target, "jail"));
                                getUserdata().setBoolean(target, "jailed", false);
                                getMessage().send(target, "&cYou got free by&f " + player.getName());
                                getMessage().send(player, "&6You freed&f " + target.getName());
                                getUserdata().setString(target, "locations.jail", null);
                            } else {
                                getJail().getLocation().getChunk().load();
                                getUserdata().setLocation(target, "jail");
                                target.teleport(getJail().getLocation());
                                getUserdata().setBoolean(target, "jailed", true);
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
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getJail().locationExist()) {
                        if (getUserdata().isJailed(target)) {
                            getUserdata().getLocation(target, "jail").getChunk().load();
                            target.teleport(getUserdata().getLocation(target, "jail"));
                            getUserdata().setBoolean(target, "jailed", false);
                            getMessage().send(target, "&cYou got free by&f " + consoleCommandSender.getName());
                            getMessage().send(consoleCommandSender, "&6You freed&f " + target.getName());
                            getUserdata().setString(target, "locations.jail", null);
                        } else {
                            getJail().getLocation().getChunk().load();
                            getUserdata().setLocation(target, "jail");
                            target.teleport(getJail().getLocation());
                            getUserdata().setBoolean(target, "jailed", true);
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
                for (Player players : getServer().getOnlinePlayers()) {
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
