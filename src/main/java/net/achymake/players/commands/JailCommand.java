package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Jail;
import net.achymake.players.files.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JailCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Jail getJail() {
        return Players.getJail();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            getMessage().send(player, "&cUsage:&f /jail target");
        }
        if (args.length == 1) {
            Player target = sender.getServer().getPlayerExact(args[0]);
            if (target != null) {
                if (getJail().jailExist()) {
                    if (target == sender) {
                        execute(sender, target);
                    } else if (!target.hasPermission("players.command.jail.exempt")) {
                        execute(sender, target);
                    }
                }
            }
        }
        return true;
    }
    private void execute(CommandSender sender, Player target) {
        if (getDatabase().isJailed(target)) {
            getDatabase().getLocation(target, "jail").getChunk().load();
            target.teleport(getDatabase().getLocation(target, "jail"));
            getDatabase().setBoolean(target, "jailed", false);
            getMessage().send(target, "&cYou got free by&f " + sender.getName());
            getMessage().send(sender, "&6You freed&f " + target.getName());
            getDatabase().setString(target, "locations.jail", null);
        } else {
            getJail().getJail().getChunk().load();
            getDatabase().setLocation(target, "jail");
            target.teleport(getJail().getJail());
            getDatabase().setBoolean(target, "jailed", true);
            getMessage().send(target, "&cYou got jailed by&f " + sender.getName());
            getMessage().send(sender, "&6You jailed&f " + target.getName());
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                for (Player players : sender.getServer().getOnlinePlayers()) {
                    if (!players.hasPermission("players.command.jail.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(sender.getName());
            }
        }
        return commands;
    }
}