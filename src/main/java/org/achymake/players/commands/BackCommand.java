package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public BackCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                return false;
            } else {
                if (args.length == 0) {
                    teleportBack(player);
                }
                if (args.length == 1) {
                    if (player.hasPermission("players.command.back.others")) {
                        Player target = getServer().getPlayerExact(args[0]);
                        if (target != null) {
                            if (target == player) {
                                teleportBack(player);
                            } else {
                                if (!target.hasPermission("players.command.back.exempt")) {
                                    teleportBack(target);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (getUserdata().isFrozen(target) || getUserdata().isJailed(target)) {
                    return false;
                } else {
                    if (target != null) {
                        teleportBack(target);
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
                if (player.hasPermission("players.command.back.others")) {
                    for (Player players : getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.back.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
    private void teleportBack(Player player) {
        if (getUserdata().locationExist(player, "death")) {
            if (player.hasPermission("players.command.back.death")) {
                getUserdata().teleport(player, "death", getUserdata().getLocation(player, "death"));
                getUserdata().setString(player, "locations.death", null);
            } else {
                getUserdata().teleport(player, "recent", getUserdata().getLocation(player, "recent"));
            }
        } else {
            String worldName = getUserdata().getLocation(player, "recent").getWorld().getName();
            if (player.hasPermission("players.command.back.world." + worldName)) {
                getUserdata().teleport(player, "recent", getUserdata().getLocation(player, "recent"));
            }
        }
    }
}
