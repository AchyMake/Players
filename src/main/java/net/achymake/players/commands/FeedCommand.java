package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    private FileConfiguration getConfig() {
        return Players.getInstance().getConfig();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (getDatabase().getCommandCooldown().containsKey("feed-" + player.getUniqueId())) {
                    Long timeElapsed = System.currentTimeMillis() - getDatabase().getCommandCooldown().get("feed-" + player.getUniqueId());
                    String cooldownTimer = getConfig().getString("commands.cooldown.feed");
                    Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
                    if (timeElapsed > integer) {
                        getDatabase().getCommandCooldown().put("feed-" + player.getUniqueId(), System.currentTimeMillis());
                        player.setFoodLevel(20);
                        getMessage().sendActionBar((Player) sender, "&6Your starvation has been satisfied");
                    } else {
                        long timer = (integer-timeElapsed);
                        getMessage().sendActionBar((Player) sender, "&cYou have to wait&f " + String.valueOf(timer).substring(0, String.valueOf(timer).length() - 3) + "&c seconds");
                    }
                } else {
                    getDatabase().getCommandCooldown().put("feed-" + player.getUniqueId(), System.currentTimeMillis());
                    player.setFoodLevel(20);
                    getMessage().sendActionBar((Player) sender, "&6Your starvation has been satisfied");
                }
            }
            if (args.length == 1) {
                if (sender.hasPermission("players.command.feed.others")) {
                    Player target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        target.setFoodLevel(20);
                        getMessage().sendActionBar(target, "&6Your starvation has been satisfied by&f " + sender.getName());
                        getMessage().send(sender, "&6You satisfied&f " + target.getName() + "&6's starvation");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    getMessage().sendActionBar(target, "&6Your starvation has been satisfied");
                    getMessage().send(sender, "You satisfied " + target.getName() + "'s starvation");
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
                if (sender.hasPermission("players.command.feed.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}