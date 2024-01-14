package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.files.Jail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetJailCommand implements CommandExecutor, TabCompleter {
    private Jail getJail() {
        return Players.getJail();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (getJail().locationExist()) {
                    getJail().setLocation(player.getLocation());
                    Players.send(player, "&6Jail relocated");
                } else {
                    getJail().setLocation(player.getLocation());
                    Players.send(player, "&6Jail has been set");
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
