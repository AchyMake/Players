package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Spawn;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Spawn getSpawn() {
        return Players.getSpawn();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    return false;
                } else {
                    if (getSpawn().locationExist()) {
                        getSpawn().getLocation().getChunk().load();
                        Players.send(player, "&6Teleporting to&f spawn");
                        player.teleport(getSpawn().getLocation());
                    } else {
                        Players.send(player, "Spawn&c does not exist");
                    }
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.spawn.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                            return false;
                        } else {
                            if (getSpawn().locationExist()) {
                                getSpawn().getLocation().getChunk().load();
                                Players.send(target, "&6Teleporting to&f spawn");
                                target.teleport(getSpawn().getLocation());
                                Players.send(player, "&6You teleported&f " + target.getName() + "&6 to&f spawn");
                            } else {
                                Players.send(player, "Spawn&c does not exist");
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) sender;
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                        return false;
                    } else {
                        if (getSpawn().locationExist()) {
                            getSpawn().getLocation().getChunk().load();
                            Players.send(target, "&6Teleporting to&f spawn");
                            target.teleport(getSpawn().getLocation());
                            Players.send(consoleCommandSender, "You teleported " + target.getName() + " to spawn");
                        } else {
                            Players.send(consoleCommandSender, "Spawn&c does not exist");
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
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("players.command.spawn.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
