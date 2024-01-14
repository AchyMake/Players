package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TPDenyCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (getDatabase().getConfig(player).isString("tpa.from")) {
                    Player target = player.getServer().getPlayer(UUID.fromString(getDatabase().getConfig(player).getString("tpa.from")));
                    if (target != null) {
                        if (player.getServer().getScheduler().isQueued(getDatabase().getConfig(target).getInt("task.tpa"))) {
                            player.getServer().getScheduler().cancelTask(getDatabase().getConfig(target).getInt("task.tpa"));
                            Players.send(target, player.getName() + "&6 denied tpa request");
                            Players.send(player, "&6You denied tpa request");
                            getDatabase().setString(target, "tpa.sent", null);
                            getDatabase().setString(target, "task.tpa", null);
                            getDatabase().setString(player, "tpa.from", null);
                        }
                    }
                } else {
                    Players.send(player, "&cYou haven't have any tpa request");
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
