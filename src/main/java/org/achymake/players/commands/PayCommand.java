package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public PayCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getUserdata().exist(offlinePlayer)) {
                    double amount = Double.parseDouble(args[1]);
                    if (amount >= getConfig().getDouble("economy.minimum-payment")) {
                        if (getEconomy().has(player, amount)) {
                            getEconomy().remove(player, amount);
                            getEconomy().add(offlinePlayer, amount);
                            getMessage().send(player, "&6You paid&f " + offlinePlayer.getName() + "&a " + getEconomy().currency() + getEconomy().format(amount));
                        } else {
                            getMessage().send(player, "&cYou don't have&a " + getEconomy().currency() + getEconomy().format(amount) + "&c to pay&f " + offlinePlayer.getName());
                        }
                    } else {
                        getMessage().send(player, "&cYou have to pay at least&a " + getEconomy().currency() + getEconomy().format(getConfig().getDouble("economy.minimum-payment")));
                    }
                } else {
                    getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
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
                for (Player players : getServer().getOnlinePlayers()) {
                    if (!plugin.getVanished().contains(players)) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
