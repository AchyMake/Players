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

public class HealCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (database.getCommandCooldown().containsKey("heal-" + player.getUniqueId())) {
                    Long timeElapsed = System.currentTimeMillis() - database.getCommandCooldown().get("heal-" + player.getUniqueId());
                    String cooldownTimer = Players.getInstance().getConfig().getString("commands.cooldown.heal");
                    Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
                    if (timeElapsed > integer) {
                        database.getCommandCooldown().put("heal-" + player.getUniqueId(), System.currentTimeMillis());
                        player.setFoodLevel(20);
                        player.setHealth(player.getMaxHealth());
                        message.send(player, "&6Your health has been satisfied");
                    } else {
                        long timer = (integer-timeElapsed);
                        message.send(player, "&cYou have to wait&f " + String.valueOf(timer).substring(0, String.valueOf(timer).length()-3) + "&c seconds");
                    }
                } else {
                    database.getCommandCooldown().put("heal-" + player.getUniqueId(), System.currentTimeMillis());
                    player.setFoodLevel(20);
                    player.setHealth(player.getMaxHealth());
                    message.send(player, "&6Your health has been satisfied");
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.heal.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    target.setHealth(target.getMaxHealth());
                    message.send(target, "&6Your health has been satisfied by&f " + sender.getName());
                    message.send(sender, "&6You satisfied&f " + target.getName() + "&6's health");
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
                if (sender.hasPermission("players.command.heal.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}