package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Spawn;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Spawn getSpawn() {
        return plugin.getSpawn();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public SpawnCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                    return false;
                } else {
                    if (getSpawn().locationExist()) {
                        getUserdata().teleport(player, "spawn", getSpawn().getLocation());
                    } else {
                        getMessage().send(player, "Spawn&c does not exist");
                    }
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.spawn.others")) {
                    Player target = getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (getUserdata().isFrozen(target) || getUserdata().isJailed(target)) {
                            return false;
                        } else {
                            if (getSpawn().locationExist()) {
                                getUserdata().teleport(player, "spawn", getSpawn().getLocation());
                            } else {
                                getMessage().send(player, "Spawn&c does not exist");
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getUserdata().isFrozen(target) || getUserdata().isJailed(target)) {
                        return false;
                    } else {
                        if (getSpawn().locationExist()) {
                            getUserdata().teleport(target, "spawn", getSpawn().getLocation());
                        } else {
                            getMessage().send(consoleCommandSender, "Spawn&c does not exist");
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
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.spawn.others")) {
                    for (Player players : getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
