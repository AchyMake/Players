package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPACommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                message.send(player, "&cUsage:&f /tpa target");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (database.isFrozen(player) || database.isJailed(player)) {
                    return false;
                } else {
                    Player target = sender.getServer().getPlayerExact(args[0]);
                    if (target == null) {
                        message.send(player, args[0] + "&c is currently offline");
                    } else if (target == player) {
                        message.send(player, "&cYou can't send request to your self");
                    } else if (database.getConfig(player).isString("tpa.sent")) {
                        message.send(player, "&cYou already sent tpa request");
                        message.send(player, "&cYou can type&f /tpcancel");
                    } else {
                        int taskID = player.getServer().getScheduler().runTaskLater(Players.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                database.setString(target, "tpa.from", null);
                                database.setString(player, "tpa.sent", null);
                                database.setString(player, "task.tpa", null);
                                message.send(player, "&cTeleport request has expired");
                                message.send(target, "&cTeleport request has expired");
                            }
                        }, 300).getTaskId();
                        database.setString(target, "tpa.from", player.getUniqueId().toString());
                        database.setString(player, "tpa.sent", target.getUniqueId().toString());
                        database.setInt(player, "task.tpa", taskID);
                        message.send(target, player.getName() + "&6 has sent you a tpa request");
                        message.send(target, "&6You can type&a /tpaccept&6 or&c /tpdeny");
                        message.send(player, "&6You have sent a tpa request to&f " + target.getName());
                        message.send(player, "&6You can type&c /tpcancel");
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