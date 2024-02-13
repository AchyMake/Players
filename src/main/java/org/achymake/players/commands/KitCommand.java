package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Kits;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Kits getKits() {
        return plugin.getKits();
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
    public KitCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&6Kits:");
                for (String kitNames : getKits().getKits()) {
                    if (player.hasPermission("players.command.kit." + kitNames)) {
                        getMessage().send(player, "- " + kitNames);
                    }
                }
            }
            if (args.length == 1) {
                String kitName = args[0].toLowerCase();
                if (player.hasPermission("players.command.kit." + kitName)) {
                    if (getKits().hasCooldown(player, kitName)) {
                        getMessage().sendActionBar(player, "&cYou have to wait&f " + getKits().getCooldown(player, kitName) + "&c seconds");
                    } else {
                        if (getKits().hasPrice(kitName)) {
                            if (getEconomy().has(player, getKits().cost(kitName))) {
                                getKits().giveKit(player, kitName);
                                getEconomy().remove(player, getKits().cost(kitName));
                                getKits().addCooldown(player, kitName);
                                getMessage().send(player, "&6You received&f " + kitName);
                            } else {
                                getMessage().send(player, "&cYou do not have&a " + getEconomy().currency() + getEconomy().format(getKits().cost(kitName)) + "&c for&f " + kitName);
                            }
                        } else {
                            getKits().giveKit(player, kitName);
                            getKits().addCooldown(player, kitName);
                            getMessage().send(player, "&6You received&f " + kitName);
                        }
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.kit.others")) {
                    Player target = getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        getKits().giveKit(target, args[0]);
                        getMessage().send(target, "&6You received&f " + args[0] + "&6 kit");
                        getMessage().send(player, "&6You gave&f " + args[0] + "&6 kit to&f " + target.getName());
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().send(consoleCommandSender, "Kits:");
                for (String kitNames : getKits().getKits()) {
                    getMessage().send(consoleCommandSender, "- " + kitNames);
                }
            }
            if (args.length == 2) {
                Player target = getServer().getPlayerExact(args[1]);
                if (target != null) {
                    getKits().giveKit(target, args[0]);
                    getMessage().send(target, "&6You received&f " + args[0] + "&6 kit");
                    getMessage().send(consoleCommandSender, "You gave " + args[0] + " kit to " + target.getName());
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
                for (String kitName : getKits().getKits()) {
                    if (player.hasPermission("players.command.kit." + kitName)) {
                        commands.add(kitName);
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.kit.others")) {
                    for (Player players : getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
