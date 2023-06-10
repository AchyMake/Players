package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersCommand implements CommandExecutor, TabCompleter {
    private final Players plugin = Players.getInstance();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            message.send(sender, "&6" + plugin.getName() + " " + plugin.getDescription().getVersion());
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                message.send(sender, "&6Players:&f config files reloaded");
            }
            if (args[0].equalsIgnoreCase("discord")) {
                message.send(sender, "&9Developers Discord");
                message.send(sender, "https://discord.com/invite/aMtQFeJKyB");
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (args[1].equalsIgnoreCase("players")) {
                    plugin.reloadPlayerFiles();
                    message.send(sender, "&6Players:&f player files reloaded");
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
                commands.add("reload");
                commands.add("discord");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    commands.add("players");
                }
            }
        }
        return commands;
    }
}