package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand implements CommandExecutor, TabCompleter {
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (player.getInventory().getItemInMainHand().getType().isAir()) {
                    Players.send(player,"&cYou have to hold an item");
                } else {
                    if (getDatabase().getCommandCooldown().containsKey("repair-" + player.getUniqueId())) {
                        Long timeElapsed = System.currentTimeMillis() - getDatabase().getCommandCooldown().get("repair-" + player.getUniqueId());
                        String cooldownTimer = getConfig().getString("commands.cooldown.repair");
                        Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
                        if (timeElapsed > integer) {
                            getDatabase().getCommandCooldown().put("repair-" + player.getUniqueId(), System.currentTimeMillis());
                            Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
                            damageable.setDamage(0);
                            player.getInventory().getItemInMainHand().setItemMeta(damageable);
                            Players.send(player, "&6You repaired&f " + player.getInventory().getItemInMainHand().getType());
                        } else {
                            long timer = (integer-timeElapsed);
                            Players.send(player, "&cYou have to wait&f " + String.valueOf(timer).substring(0, String.valueOf(timer).length()-3) + "&c seconds");
                        }
                    } else {
                        getDatabase().getCommandCooldown().put("repair-" + player.getUniqueId(), System.currentTimeMillis());
                        Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
                        damageable.setDamage(0);
                        player.getInventory().getItemInMainHand().setItemMeta(damageable);
                        Players.send(player, "&6You repaired&f " + player.getInventory().getItemInMainHand().getType());
                    }
                }
            }
            if (args.length == 1) {
                if (sender.hasPermission("players.command.repair.force")) {
                    if (args[0].equalsIgnoreCase("force")) {
                        if (player.getInventory().getItemInMainHand().getType().isAir()) {
                            Players.send(player,"&cYou have to hold an item");
                        } else {
                            Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
                            damageable.setDamage(0);
                            player.getInventory().getItemInMainHand().setItemMeta(damageable);
                            Players.send(player, "&6You repaired&f " + player.getInventory().getItemInMainHand().getType());
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (target.getInventory().getItemInMainHand().getType().isAir()) {
                        Players.send(target,"&cYou have to hold an item");
                    } else {
                        Damageable damageable = (Damageable) target.getInventory().getItemInMainHand().getItemMeta();
                        damageable.setDamage(0);
                        target.getInventory().getItemInMainHand().setItemMeta(damageable);
                        Players.send(commandSender, "You repaired " + target.getInventory().getItemInMainHand().getType());
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
                if (player.hasPermission("players.command.repair.force")) {
                    commands.add("force");
                }
            }
        }
        return commands;
    }
}