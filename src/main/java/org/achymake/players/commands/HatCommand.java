package org.achymake.players.commands;

import org.achymake.players.Players;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class HatCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (!player.getInventory().getItemInMainHand().getType().isAir()) {
                    if (player.getInventory().getHelmet() == null) {
                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                        Players.send(player, "&6You are now wearing&f " + heldItem.getType());
                        ItemStack itemStack = new ItemStack(heldItem.getType(), 1);
                        player.getInventory().setHelmet(itemStack);
                        heldItem.setAmount(heldItem.getAmount() - 1);
                    } else {
                        Players.send(player, "&cYou are already wearing&f " + player.getInventory().getHelmet().getType());
                    }
                } else {
                    Players.send(player, "&cYou have to hold an item");
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
