package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TPCancelCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (database.getConfig(player).isString("tpa.sent")) {
                    Player target = player.getServer().getPlayer(UUID.fromString(database.getConfig(player).getString("tpa.sent")));
                    if (target != null) {
                        if (player.getServer().getScheduler().isQueued(database.getConfig(player).getInt("task.tpa"))) {
                            player.getServer().getScheduler().cancelTask(database.getConfig(player).getInt("task.tpa"));
                            message.send(target, player.getName() + "&6 cancelled tpa request");
                            message.send(player, "&6You cancelled tpa request");
                            database.setString(target, "tpa.from", null);
                            database.setString(player, "task.tpa", null);
                            database.setString(player, "tpa.sent", null);
                        }
                    }
                } else {
                    message.send(player, "&cYou haven't sent any tpa request");
                }
            }
        }
        return true;
    }
    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.EMPTY_LIST;
    }
}