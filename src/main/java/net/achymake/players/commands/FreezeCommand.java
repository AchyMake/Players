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

public class FreezeCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            getMessage().send(sender, "&cUsage:&f /freeze target");
        }
        if (args.length == 1) {
            Player player = (Player) sender;
            Player target = player.getServer().getPlayerExact(args[0]);
            if (target == sender) {
                getDatabase().setBoolean(target, "settings.frozen", !getDatabase().isFrozen(target));
                if (getDatabase().isFrozen(target)) {
                    getMessage().send(sender, "&6You froze&f " + target.getName());
                } else {
                    getMessage().send(sender, "&6You unfroze&f " + target.getName());
                }
            } else {
                if (target != null) {
                    if (!target.hasPermission("players.command.freeze.exempt")) {
                        getDatabase().setBoolean(target, "settings.frozen", !getDatabase().isFrozen(target));
                        if (getDatabase().isFrozen(target)) {
                            getMessage().send(sender, "&6You froze&f " + target.getName());
                        } else {
                            getMessage().send(sender, "&6You unfroze&f " + target.getName());
                        }
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (getDatabase().exist(offlinePlayer)) {
                        getDatabase().setBoolean(offlinePlayer, "settings.frozen", !getDatabase().isFrozen(offlinePlayer));
                        if (getDatabase().isFrozen(offlinePlayer)) {
                            getMessage().send(sender, "&6You froze&f " + offlinePlayer.getName());
                        } else {
                            getMessage().send(sender, "&6You unfroze&f " + offlinePlayer.getName());
                        }
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
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
                    if (!players.hasPermission("players.command.freeze.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(sender.getName());
            }
        }
        return commands;
    }
}