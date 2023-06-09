package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import net.achymake.players.files.Spawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Spawn spawn = Players.getSpawn();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (database.isFrozen(player) || database.isJailed(player)) {
                    return false;
                } else {
                    if (spawn.spawnExist()) {
                        spawn.getSpawn().getChunk().load();
                        message.send(sender, "&6Teleporting to&f spawn");
                        player.teleport(spawn.getSpawn());
                    }
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("smpcore.command.spawn.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (database.isFrozen(target) || database.isJailed(target)) {
                        return false;
                    } else {
                        if (spawn.spawnExist()) {
                            spawn.getSpawn().getChunk().load();
                            message.send(target, "&6Teleporting to&f spawn");
                            target.teleport(spawn.getSpawn());
                            message.send(sender, "&6You teleported&f " + target.getName() + "&6 to&f spawn");
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
                if (sender.hasPermission("smpcore.command.spawn.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}