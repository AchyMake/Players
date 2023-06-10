package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class HatCommand implements CommandExecutor, TabCompleter {
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (player.getInventory().getHelmet() != null) {
                    if (!player.getInventory().getItemInMainHand().getType().isAir()) {
                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                        ItemStack itemStack = new ItemStack(heldItem.getType(), 1);
                        player.getInventory().setHelmet(itemStack);
                        heldItem.setAmount(heldItem.getAmount() - 1);
                    } else {
                        message.send(player, "&cYou have to hold an item");
                    }
                } else {
                    message.send(player, "&cYou are already wearing&f " + player.getInventory().getHelmet().getType());
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