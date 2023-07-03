package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPACommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Players getPlugin() {
        return Players.getInstance();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /tpa target");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                    return false;
                } else {
                    Player target = sender.getServer().getPlayerExact(args[0]);
                    if (target == null) {
                        Players.send(player, args[0] + "&c is currently offline");
                    } else if (target == player) {
                        Players.send(player, "&cYou can't send request to your self");
                    } else if (getDatabase().getConfig(player).isString("tpa.sent")) {
                        Players.send(player, "&cYou already sent tpa request");
                        Players.send(player, "&cYou can type&f /tpcancel");
                    } else {
                        int taskID = player.getServer().getScheduler().runTaskLater(getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                getDatabase().setString(target, "tpa.from", null);
                                getDatabase().setString(player, "tpa.sent", null);
                                getDatabase().setString(player, "task.tpa", null);
                                Players.send(player, "&cTeleport request has expired");
                                Players.send(target, "&cTeleport request has expired");
                            }
                        }, 300).getTaskId();
                        getDatabase().setString(target, "tpa.from", player.getUniqueId().toString());
                        getDatabase().setString(player, "tpa.sent", target.getUniqueId().toString());
                        getDatabase().setInt(player, "task.tpa", taskID);
                        Players.send(target, player.getName() + "&6 has sent you a tpa request");
                        Players.send(target, "&6You can type&a /tpaccept&6 or&c /tpdeny");
                        Players.send(player, "&6You have sent a tpa request to&f " + target.getName());
                        Players.send(player, "&6You can type&c /tpcancel");
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
                Player player = (Player) sender;
                for (Player players : player.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}