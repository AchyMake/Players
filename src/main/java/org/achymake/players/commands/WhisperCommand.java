package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WhisperCommand implements CommandExecutor, TabCompleter {
    private Players getPlugin() {
        return Players.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getDatabase().isMuted(player) || getDatabase().isJailed(player)) {
                return false;
            }
            if (args.length == 0) {
                getMessage().send(player, "&cUsage:&f /whisper target message");
            }
            if (args.length > 1) {
                Player target = player.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }
                    getMessage().send(player, "&7You > " + target.getName() + ": " + stringBuilder.toString().strip());
                    getMessage().send(target, "&7" + player.getName() + " > You: " + stringBuilder.toString().strip());
                    getDatabase().setString(target, "last-whisper", target.getUniqueId().toString());
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (players.hasPermission("players.notify.whispers")) {
                            getMessage().send(players, "&7" + player.getName() + " > " + target.getName() + ": " + stringBuilder.toString().strip());
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
                for (Player players : player.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}
