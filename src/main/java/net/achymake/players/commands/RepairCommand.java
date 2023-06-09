package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (player.getInventory().getItemInMainHand().getType().isAir()) {
                    message.send(player,"&cYou have to hold an item");
                } else {
                    if (database.getCommandCooldown().containsKey("repair-" + player.getUniqueId())) {
                        Long timeElapsed = System.currentTimeMillis() - database.getCommandCooldown().get("repair-" + player.getUniqueId());
                        String cooldownTimer = Players.getInstance().getConfig().getString("commands.cooldown.repair");
                        Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
                        if (timeElapsed > integer) {
                            database.getCommandCooldown().put("repair-" + player.getUniqueId(), System.currentTimeMillis());
                            Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
                            damageable.setDamage(0);
                            player.getInventory().getItemInMainHand().setItemMeta(damageable);
                            message.send(sender, "&6You repaired&f " + player.getInventory().getItemInMainHand().getType());
                        } else {
                            long timer = (integer-timeElapsed);
                            message.send(sender, "&cYou have to wait&f " + String.valueOf(timer).substring(0, String.valueOf(timer).length()-3) + "&c seconds");
                        }
                    } else {
                        database.getCommandCooldown().put("repair-" + player.getUniqueId(), System.currentTimeMillis());
                        Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
                        damageable.setDamage(0);
                        player.getInventory().getItemInMainHand().setItemMeta(damageable);
                        message.send(sender, "&6You repaired&f " + player.getInventory().getItemInMainHand().getType());
                    }
                }
            }
            if (args.length == 1) {
                if (sender.hasPermission("players.command.repair.force")) {
                    if (args[0].equalsIgnoreCase("force")) {
                        if (player.getInventory().getItemInMainHand().getType().isAir()) {
                            message.send(player,"&cYou have to hold an item");
                        }else{
                            Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
                            damageable.setDamage(0);
                            player.getInventory().getItemInMainHand().setItemMeta(damageable);
                            message.send(sender, "&6You repaired&f " + player.getInventory().getItemInMainHand().getType());
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (target.getInventory().getItemInMainHand().getType().isAir()) {
                        message.send(target,"&cYou have to hold an item");
                    } else {
                        Damageable damageable = (Damageable) target.getInventory().getItemInMainHand().getItemMeta();
                        damageable.setDamage(0);
                        target.getInventory().getItemInMainHand().setItemMeta(damageable);
                        message.send(sender, "&6You repaired&f " + target.getInventory().getItemInMainHand().getType());
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