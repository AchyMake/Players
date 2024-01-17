package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TPCancelCommand implements CommandExecutor, TabCompleter {
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
            if (args.length == 0) {
                if (getDatabase().getConfig(player).isString("tpa.sent")) {
                    Player target = player.getServer().getPlayer(UUID.fromString(getDatabase().getConfig(player).getString("tpa.sent")));
                    if (target != null) {
                        if (player.getServer().getScheduler().isQueued(getDatabase().getConfig(player).getInt("task.tpa"))) {
                            player.getServer().getScheduler().cancelTask(getDatabase().getConfig(player).getInt("task.tpa"));
                            getMessage().send(target, player.getName() + "&6 cancelled tpa request");
                            getMessage().send(player, "&6You cancelled tpa request");
                            getDatabase().setString(target, "tpa.from", null);
                            getDatabase().setString(player, "task.tpa", null);
                            getDatabase().setString(player, "tpa.sent", null);
                        }
                    }
                } else {
                    getMessage().send(player, "&cYou haven't sent any tpa request");
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
