package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HealCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (getDatabase().getCommandCooldown().containsKey("heal-" + player.getUniqueId())) {
                    Long timeElapsed = System.currentTimeMillis() - getDatabase().getCommandCooldown().get("heal-" + player.getUniqueId());
                    String cooldownTimer = getConfig().getString("commands.cooldown.heal");
                    Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
                    if (timeElapsed > integer) {
                        getDatabase().getCommandCooldown().put("heal-" + player.getUniqueId(), System.currentTimeMillis());
                        player.setFoodLevel(20);
                        player.setHealth(player.getMaxHealth());
                        Players.sendActionBar(player, "&6Your health has been satisfied");
                    } else {
                        long timer = (integer-timeElapsed);
                        Players.send(player, "&cYou have to wait&f " + String.valueOf(timer).substring(0, String.valueOf(timer).length()-3) + "&c seconds");
                    }
                } else {
                    getDatabase().getCommandCooldown().put("heal-" + player.getUniqueId(), System.currentTimeMillis());
                    player.setFoodLevel(20);
                    player.setHealth(player.getMaxHealth());
                    Players.sendActionBar(player, "&6Your health has been satisfied");
                }
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.heal.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        target.setFoodLevel(20);
                        target.setHealth(target.getMaxHealth());
                        Players.sendActionBar(target, "&6Your health has been satisfied by&f " + player.getName());
                        Players.send(player, "&6You satisfied&f " + target.getName() + "&6's health");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    target.setHealth(target.getMaxHealth());
                    Players.sendActionBar(target, "&6Your health has been satisfied");
                    Players.send(commandSender, "You satisfied " + target.getName() + "'s health");
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