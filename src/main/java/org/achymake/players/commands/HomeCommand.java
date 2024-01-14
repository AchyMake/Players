package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Database;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (getDatabase().isFrozen(player) || getDatabase().isJailed(player)) {
                return false;
            } else {
                if (args.length == 0) {
                    if (getDatabase().homeExist(player, "home")) {
                        getDatabase().getHome(player, "home").getChunk().load();
                        player.teleport(getDatabase().getHome(player, "home"));
                        Players.sendActionBar(player, "&6Teleporting to&f home");
                    } else {
                        Players.send(player, "home&c does not exist");
                    }
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("bed")) {
                        if (player.hasPermission("players.command.home.bed")) {
                            if (player.getBedSpawnLocation() != null){
                                Location location = player.getBedSpawnLocation();
                                location.setPitch(player.getLocation().getPitch());
                                location.setYaw(player.getLocation().getYaw());
                                player.getBedSpawnLocation().getChunk().load();
                                Players.sendActionBar(player, "&6Teleporting to&f " + args[0]);
                                player.teleport(location);
                            } else {
                                Players.send(player, args[0] + "&c does not exist");
                            }
                        }
                    } else {
                        if (getDatabase().homeExist(player, args[0])) {
                            getDatabase().getHome(player, args[0]).getChunk().load();
                            player.teleport(getDatabase().getHome(player, args[0]));
                            Players.sendActionBar(player, "&6Teleporting to&f " + args[0]);
                        } else {
                            Players.send(player, args[0] + "&c does not exist");
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
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("players.commands.home.bed")) {
                    commands.add("bed");
                }
                commands.addAll(getDatabase().getHomes(player));
            }
        }
        return commands;
    }
}
