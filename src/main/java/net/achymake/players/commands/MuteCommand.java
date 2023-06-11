package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MuteCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Players.getMessage().send(sender, "&cUsage:&f /mute target");
        }
        if (args.length == 1) {
            Player target = sender.getServer().getPlayerExact(args[0]);
            if (target == sender) {
                getDatabase().setBoolean(target, "settings.muted", !getDatabase().isMuted(target));
                if (getDatabase().isMuted(target)) {
                    getMessage().send(sender, "&6You muted your self");
                } else {
                    getMessage().send(sender, "&6You unmuted your self");
                }
            } else {
                if (target != null) {
                    if (!target.hasPermission("players.command.mute.exempt")) {
                        getDatabase().setBoolean(target, "settings.muted", !getDatabase().isMuted(target));
                        if (getDatabase().isMuted(target)) {
                            getMessage().send(sender, "&6You muted&f " + target.getName());
                        } else {
                            getMessage().send(sender, "&6You unmuted&f " + target.getName());
                        }
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setBoolean(offlinePlayer, "settings.muted", !getDatabase().isMuted(offlinePlayer));
                        if (getDatabase().isMuted(offlinePlayer)) {
                            getMessage().send(sender, "&6You muted&f " + offlinePlayer.getName());
                        } else {
                            getMessage().send(sender, "&6You unmuted&f " + offlinePlayer.getName());
                        }
                    } else {
                        getMessage().send(sender,offlinePlayer.getName() + "&c has never joined");
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
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    if (!players.hasPermission("players.command.mute.exempt")) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}