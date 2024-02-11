package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Worth;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorthCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Worth getWorth() {
        return plugin.getWorth();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public WorthCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                Material material = Material.valueOf(args[0].toUpperCase());
                if (getWorth().isSellable(material)) {
                    getMessage().send(player, material + "&6 is worth:&a " + getEconomy().currency() + getEconomy().format(getWorth().getWorth(material)));
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
                for (String worthList : getWorth().getList()) {
                    commands.add(worthList.toLowerCase());
                }
            }
        }
        return commands;
    }
}