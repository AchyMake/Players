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

public class FeedCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (database.getCommandCooldown().containsKey("feed-" + player.getUniqueId())) {
                    Long timeElapsed = System.currentTimeMillis() - database.getCommandCooldown().get("feed-" + player.getUniqueId());
                    String cooldownTimer = Players.getInstance().getConfig().getString("commands.cooldown.feed");
                    Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
                    if (timeElapsed > integer) {
                        database.getCommandCooldown().put("feed-" + player.getUniqueId(), System.currentTimeMillis());
                        player.setFoodLevel(20);
                        message.sendActionBar((Player) sender, "&6Your starvation has been satisfied");
                    } else {
                        long timer = (integer-timeElapsed);
                        message.sendActionBar((Player) sender, "&cYou have to wait&f " + String.valueOf(timer).substring(0, String.valueOf(timer).length() - 3) + "&c seconds");
                    }
                } else {
                    database.getCommandCooldown().put("feed-" + player.getUniqueId(), System.currentTimeMillis());
                    player.setFoodLevel(20);
                    message.sendActionBar((Player) sender, "&6Your starvation has been satisfied");
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.feed.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    message.sendActionBar(target, "&6Your starvation has been satisfied by&f " + sender.getName());
                    message.send(sender, "&6You satisfied&f " + target.getName() + "&6's starvation");
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