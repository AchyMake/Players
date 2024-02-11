package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MuteCommand implements CommandExecutor, TabCompleter {
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
    public MuteCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target == player) {
                    getUserdata().setBoolean(target, "settings.muted", !getUserdata().isMuted(target));
                    if (getUserdata().isMuted(target)) {
                        getMessage().send(player, "&6You muted your self");
                    } else {
                        getMessage().send(player, "&6You unmuted your self");
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.mute.exempt")) {
                            getUserdata().setBoolean(target, "settings.muted", !getUserdata().isMuted(target));
                            if (getUserdata().isMuted(target)) {
                                getMessage().send(player, "&6You muted&f " + target.getName());
                            } else {
                                getMessage().send(player, "&6You unmuted&f " + target.getName());
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (getUserdata().exist(offlinePlayer)) {
                            getUserdata().setBoolean(offlinePlayer, "settings.muted", !getUserdata().isMuted(offlinePlayer));
                            if (getUserdata().isMuted(offlinePlayer)) {
                                getMessage().send(player, "&6You muted&f " + offlinePlayer.getName());
                            } else {
                                getMessage().send(player, "&6You unmuted&f " + offlinePlayer.getName());
                            }
                        } else {
                            getMessage().send(player,offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getUserdata().setBoolean(target, "settings.muted", !getUserdata().isMuted(target));
                    if (getUserdata().isMuted(target)) {
                        getMessage().send(consoleCommandSender, "You muted " + target.getName());
                    } else {
                        getMessage().send(consoleCommandSender, "You unmuted " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getUserdata().exist(offlinePlayer)) {
                        getUserdata().setBoolean(offlinePlayer, "settings.muted", !getUserdata().isMuted(offlinePlayer));
                        if (getUserdata().isMuted(offlinePlayer)) {
                            getMessage().send(consoleCommandSender, "You muted " + offlinePlayer.getName());
                        } else {
                            getMessage().send(consoleCommandSender, "You unmuted " + offlinePlayer.getName());
                        }
                    } else {
                        getMessage().send(consoleCommandSender,offlinePlayer.getName() + " has never joined");
                    }
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
                    if (!players.hasPermission("players.command.mute.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(player.getName());
            }
        }
        return commands;
    }
}
