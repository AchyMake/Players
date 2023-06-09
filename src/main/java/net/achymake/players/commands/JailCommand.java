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
    private final Database database = Players.getDatabase();
    private final Jail jail = Players.getJail();
    private final Message message = Players.getMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            message.send(player, "&cUsage:&f /jail target");
        }
        if (args.length == 1) {
            Player target = sender.getServer().getPlayerExact(args[0]);
            if (target != null) {
                if (jail.jailExist()) {
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
        if (database.isJailed(target)) {
            database.getLocation(target, "jail").getChunk().load();
            target.teleport(database.getLocation(target, "jail"));
            database.setBoolean(target, "jailed", false);
            message.send(target, "&cYou got free by&f " + sender.getName());
            message.send(sender, "&6You freed&f " + target.getName());
            database.setString(target, "locations.jail", null);
        } else {
            jail.getJail().getChunk().load();
            database.setLocation(target, "jail");
            target.teleport(jail.getJail());
            database.setBoolean(target, "jailed", true);
            message.send(target, "&cYou got jailed by&f " + sender.getName());
            message.send(sender, "&6You jailed&f " + target.getName());
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