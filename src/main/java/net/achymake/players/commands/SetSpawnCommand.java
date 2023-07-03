package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Spawn;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {
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
                if (getSpawn().spawnExist()) {
                    getSpawn().setSpawn(player.getLocation());
                    Players.send(player, "&6Spawn relocated");
                } else {
                    getSpawn().setSpawn(player.getLocation());
                    Players.send(player, "&6Spawn has been set");
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                if (getDatabase().exist(offlinePlayer)) {
                    if (getDatabase().locationExist(offlinePlayer, "spawn")) {
                        getDatabase().setLocation(offlinePlayer, "spawn", player.getLocation());
                        Players.send(player, offlinePlayer.getName() + "&6's spawn relocated");
                    } else {
                        getDatabase().setLocation(offlinePlayer, "spawn", player.getLocation());
                        Players.send(player, offlinePlayer.getName() + "&6's spawn set");
                    }
                } else {
                    Players.send(player, offlinePlayer.getName() + "&c has never joined");
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