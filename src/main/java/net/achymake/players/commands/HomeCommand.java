package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.api.EconomyProvider;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeCommand implements CommandExecutor, TabCompleter {
    private final Database database = Players.getDatabase();
    private final EconomyProvider economyProvider = Players.getEconomyProvider();
    private final FileConfiguration config = Players.getInstance().getConfig();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (database.isFrozen(player) || database.isJailed(player)) {
                return false;
            } else {
                if (args.length == 0) {
                    if (database.homeExist(player, "home")) {
                        database.getHome(player, "home").getChunk().load();
                        player.teleport(database.getHome(player, "home"));
                        message.send(player, "&6Teleporting to&f home");
                    } else {
                        message.send(player, "home&c does not exist");
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
                                message.send(player, "&6Teleporting to&f " + args[0]);
                                player.teleport(location);
                            } else {
                                message.send(player, args[0] + "&c does not exist");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("buy")) {
                        if (player.hasPermission("players.command.home.buy")) {
                            message.send(player, "&6Homes costs&a " + economyProvider.format(config.getDouble("homes.cost")));
                        }
                    } else {
                        if (database.homeExist(player, args[0])) {
                            database.getHome(player, args[0]).getChunk().load();
                            player.teleport(database.getHome(player, args[0]));
                            message.send(player, "&6Teleporting to&f " + args[0]);
                        } else {
                            message.send(player, args[0] + "&c does not exist");
                        }
                    }
                }
                if (args.length == 2) {
                    int amount = Integer.parseInt(args[1]);
                    if (args[0].equalsIgnoreCase("buy")) {
                        if (player.hasPermission("players.command.home.buy")) {
                            if (database.getEconomy(player) >= config.getDouble("homes.cost") * amount) {
                                database.setInt(player, "max-homes", amount);
                                database.removeEconomy(player, config.getDouble("homes.cost") * amount);
                                message.send(player, "&6You bought " + amount + " homes for&a " + economyProvider.format(config.getDouble("homes.cost") * amount));
                            } else {
                                message.send(player, "&cYou don't have&a " + economyProvider.format(config.getDouble("homes.cost") * amount) + "&c to buy&f " + amount + "&c homes");
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
                if (sender.hasPermission("players.commands.home.buy")) {
                    commands.add("buy");
                }
                if (sender.hasPermission("players.commands.home.bed")) {
                    commands.add("bed");
                }
                commands.addAll(database.getHomes((Player) sender));
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("buy")) {
                    if (sender.hasPermission("players.commands.home.buy")) {
                        if (args[0].equalsIgnoreCase("buy")) {
                            commands.add("1");
                            commands.add("2");
                            commands.add("3");
                        }
                    }
                }
            }
        }
        return commands;
    }
}