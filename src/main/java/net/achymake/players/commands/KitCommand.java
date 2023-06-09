package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Kits;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    private final Kits kits = Players.getKits();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            message.send(sender, "&6Kits:");
            for (String kitNames : kits.getKits()) {
                if (sender.hasPermission("players.command.kit." + kitNames)) {
                    message.send(sender, "- " + kitNames);
                }
            }
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("players.command.kit." + args[0])) {
                    for (String kitNames : kits.getKits()) {
                        if (args[0].equals(kitNames)) {
                            kits.giveKitWithCooldown(player, args[0]);
                        }
                    }
                }
            }
        }
        if (args.length == 2) {
            if (sender.hasPermission("players.command.kit.others")) {
                Player target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    kits.giveKit(target, args[0]);
                    message.send(target, "&6You received&f " + args[0] + "&6 kit");
                    message.send(sender, "&6You dropped&f " + args[0] + "&6 kit to&f " + target.getName());
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
                for (String kitName : kits.getKits()) {
                    if (sender.hasPermission("players.command.kit." + kitName)) {
                        commands.add(kitName);
                    }
                }
            }
            if (args.length == 2) {
                if (sender.hasPermission("players.command.kit.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}