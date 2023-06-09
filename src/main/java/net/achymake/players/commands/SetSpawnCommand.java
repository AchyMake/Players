package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import net.achymake.players.files.Spawn;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Spawn spawn = Players.getSpawn();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (spawn.spawnExist()) {
                    spawn.setSpawn(player.getLocation());
                    message.send(sender, "&6Spawn relocated");
                } else {
                    spawn.setSpawn(player.getLocation());
                    message.send(sender, "&6Spawn has been set");
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                if (database.exist(offlinePlayer)) {
                    if (database.locationExist(offlinePlayer, "spawn")) {
                        database.setLocation(offlinePlayer, "spawn", player.getLocation());
                        message.send(player, offlinePlayer.getName() + "&6's spawn relocated");
                    } else {
                        database.setLocation(offlinePlayer, "spawn", player.getLocation());
                        message.send(player, offlinePlayer.getName() + "&6's spawn set");
                    }
                } else {
                    message.send(sender, offlinePlayer.getName() + "&c has never joined");
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
                if (sender.hasPermission("players.command.setspawn.others")) {
                    for (OfflinePlayer offlinePlayer : sender.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayer.getName());
                    }
                }
            }
        }
        return commands;
    }
}