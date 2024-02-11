package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Spawn;
import org.achymake.players.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {
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
    public SetSpawnCommand(Players plugin) {
        this.plugin = plugin;
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
                OfflinePlayer offlinePlayer = getServer().getOfflinePlayer(args[0]);
                if (getUserdata().exist(offlinePlayer)) {
                    if (getUserdata().locationExist(offlinePlayer, "spawn")) {
                        getUserdata().setLocation(offlinePlayer, "spawn", player.getLocation());
                        getMessage().send(player, offlinePlayer.getName() + "&6's spawn relocated");
                    } else {
                        getUserdata().setLocation(offlinePlayer, "spawn", player.getLocation());
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
                    for (OfflinePlayer offlinePlayer : getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayer.getName());
                    }
                }
            }
        }
        return commands;
    }
}
