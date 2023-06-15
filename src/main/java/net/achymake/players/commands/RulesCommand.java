package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Motd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RulesCommand implements CommandExecutor, TabCompleter {
    private Motd getMotd() {
        return Players.getMotd();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (getMotd().motdExist("rules")) {
                getMotd().sendMotd(sender, "rules");
            }
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.rules.others")) {
                Player target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getMotd().motdExist("rules")) {
                        getMotd().sendMotd(target, "rules");
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
                if (sender.hasPermission("players.command.rules.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}