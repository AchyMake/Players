package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Jail;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetJailCommand implements CommandExecutor, TabCompleter {
    private final Jail jail = Players.getJail();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (jail.jailExist()) {
                    jail.setJail(player.getLocation());
                    message.send(sender, "&6Jail relocated");
                } else {
                    jail.setJail(player.getLocation());
                    message.send(sender, "&6Jail has been set");
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