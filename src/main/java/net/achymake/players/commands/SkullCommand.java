package net.achymake.players.commands;

import net.achymake.players.Players;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkullCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                Players.send(player, "&cUsage:&f /skull offlinePlayer");
            }
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
                skullMeta.setOwningPlayer(offlinePlayer);
                skullItem.setItemMeta(skullMeta);
                if (Arrays.asList(player.getInventory().getStorageContents()).contains(null)) {
                    player.getInventory().addItem(skullItem);
                } else {
                    player.getWorld().dropItem(player.getLocation(), skullItem);
                }
                Players.send(player, "&6You received&f " + offlinePlayer.getName() + "&6's skull");
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}