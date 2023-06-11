package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GMSCommand implements CommandExecutor, TabCompleter {
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
                    player.setGameMode(GameMode.SURVIVAL);
                    getMessage().send(player, "&6You changed gamemode to&f survival");
                }
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.gamemode.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target == sender) {
                    if (!target.getGameMode().equals(GameMode.SURVIVAL)) {
                        target.setGameMode(GameMode.SURVIVAL);
                        getMessage().send(target, sender.getName() + "&6 changed your gamemode to&f survival");
                        getMessage().send(sender, "&6You changed&f " + target.getName() + "&6 gamemode to&f survival");
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.gamemode.exempt")) {
                            if (!target.getGameMode().equals(GameMode.SURVIVAL)) {
                                target.setGameMode(GameMode.SURVIVAL);
                                getMessage().send(target, sender.getName() + "&6 changed your gamemode to&f survival");
                                getMessage().send(sender, "&6You changed&f " + target.getName() + "&6 gamemode to&f survival");
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("players.command.gamemode.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.gamemode.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(sender.getName());
                }
            }
        }
        return commands;
    }
}