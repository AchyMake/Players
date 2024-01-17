package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.achymake.players.files.Spawn;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Spawn getSpawn() {
        return getPlugin().getSpawn();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getSpawn().locationExist()) {
                    getSpawn().setLocation(player.getLocation());
                    getMessage().send(player, "&6Spawn relocated");
                } else {
                    getSpawn().setLocation(player.getLocation());
                    getMessage().send(player, "&6Spawn has been set");
                }
            }
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                if (getDatabase().exist(offlinePlayer)) {
                    if (getDatabase().locationExist(offlinePlayer, "spawn")) {
                        getDatabase().setLocation(offlinePlayer, "spawn", player.getLocation());
                        getMessage().send(player, offlinePlayer.getName() + "&6's spawn relocated");
                    } else {
                        getDatabase().setLocation(offlinePlayer, "spawn", player.getLocation());
                        getMessage().send(player, offlinePlayer.getName() + "&6's spawn set");
                    }
                } else {
                    getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
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
                if (player.hasPermission("players.command.setspawn.others")) {
                    for (OfflinePlayer offlinePlayer : player.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayer.getName());
                    }
                }
            }
        }
        return commands;
    }
}
