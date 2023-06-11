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
    private FileConfiguration getConfig() {
        return Players.getInstance().getConfig();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private EconomyProvider getEconomyProvider() {
        return Players.getEconomyProvider();
    }
    private Message getMessage() {
        return Players.getMessage();
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
                        getMessage().sendActionBar(player, "&6Teleporting to&f home");
                    } else {
                        getMessage().send(player, "home&c does not exist");
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
                                getMessage().sendActionBar(player, "&6Teleporting to&f " + args[0]);
                                player.teleport(location);
                            } else {
                                getMessage().send(player, args[0] + "&c does not exist");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("buy")) {
                        if (player.hasPermission("players.command.home.buy")) {
                            getMessage().send(player, "&6Homes costs&a " + getEconomyProvider().format(getConfig().getDouble("homes.cost")));
                        }
                    } else {
                        if (getDatabase().homeExist(player, args[0])) {
                            getDatabase().getHome(player, args[0]).getChunk().load();
                            player.teleport(getDatabase().getHome(player, args[0]));
                            getMessage().sendActionBar(player, "&6Teleporting to&f " + args[0]);
                        } else {
                            getMessage().send(player, args[0] + "&c does not exist");
                        }
                    }
                }
                if (args.length == 2) {
                    int amount = Integer.parseInt(args[1]);
                    if (args[0].equalsIgnoreCase("buy")) {
                        if (player.hasPermission("players.command.home.buy")) {
                            if (getDatabase().getEconomy(player) >= getConfig().getDouble("homes.cost") * amount) {
                                getDatabase().setInt(player, "max-homes", amount);
                                getDatabase().removeEconomy(player, getConfig().getDouble("homes.cost") * amount);
                                getMessage().send(player, "&6You bought " + amount + " homes for&a " + getEconomyProvider().format(getConfig().getDouble("homes.cost") * amount));
                            } else {
                                getMessage().send(player, "&cYou don't have&a " + getEconomyProvider().format(getConfig().getDouble("homes.cost") * amount) + "&c to buy&f " + amount + "&c homes");
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
                commands.addAll(getDatabase().getHomes((Player) sender));
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