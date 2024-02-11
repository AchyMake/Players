package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public RepairCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType().isAir()) {
                    getMessage().send(player,"&cYou have to hold an item");
                } else {
                    if (getUserdata().hasCooldown(player, "repair")) {
                        getMessage().sendActionBar(player, "&cYou have to wait&f " + getUserdata().getCooldown(player, "repair") + "&c seconds");
                    } else {
                        Damageable damageable = (Damageable) itemStack.getItemMeta();
                        if (damageable.hasDamage()) {
                            damageable.setDamage(0);
                            itemStack.setItemMeta(damageable);
                            getMessage().send(player, "&6You repaired&f " + itemStack.getType());
                            getUserdata().addCooldown(player, "repair");
                        } else {
                            getMessage().send(player, "&cThe item is fully repaired");
                        }
                    }
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("force")) {
                    if (player.hasPermission("players.command.repair.force")) {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        if (itemStack.getType().isAir()) {
                            getMessage().send(player,"&cYou have to hold an item");
                        } else {
                            Damageable damageable = (Damageable) itemStack.getItemMeta();
                            if (damageable.hasDamage()) {
                                damageable.setDamage(0);
                                itemStack.setItemMeta(damageable);
                                getMessage().send(player, "&6You repaired&f " + itemStack.getType());
                            } else {
                                getMessage().send(player, "&cThe item is fully repaired");
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.repair.force")) {
                    commands.add("force");
                }
            }
        }
        return commands;
    }
}
