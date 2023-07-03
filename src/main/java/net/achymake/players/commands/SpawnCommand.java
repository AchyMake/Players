package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Spawn;
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
            if (args.length == 0) {
                Player player = (Player) sender;
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    return false;
                } else {
                    if (getSpawn().spawnExist()) {
                        getSpawn().getSpawn().getChunk().load();
                        Players.send(player, "&6Teleporting to&f spawn");
                        player.teleport(getSpawn().getSpawn());
                    } else {
                        Players.send(player, "Spawn&c does not exist");
                    }
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.spawn.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                            return false;
                        } else {
                            if (getSpawn().spawnExist()) {
                                getSpawn().getSpawn().getChunk().load();
                                Players.send(target, "&6Teleporting to&f spawn");
                                target.teleport(getSpawn().getSpawn());
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
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getDatabase().isFrozen(target) || getDatabase().isJailed(target)) {
                        return false;
                    } else {
                        if (getSpawn().spawnExist()) {
                            getSpawn().getSpawn().getChunk().load();
                            Players.send(target, "&6Teleporting to&f spawn");
                            target.teleport(getSpawn().getSpawn());
                            Players.send(commandSender, "You teleported " + target.getName() + " to spawn");
                        } else {
                            Players.send(commandSender, "Spawn&c does not exist");
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
                if (sender.hasPermission("players.command.spawn.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}