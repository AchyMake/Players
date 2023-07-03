package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RespondCommand implements CommandExecutor, TabCompleter {
    private Database getDatabase() {
        return Players.getDatabase();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                Players.send(player, "&cUsage:&f /r message");
            }
            Player player = (Player) sender;
            if (!getDatabase().isMuted(player)) {
                if (args.length > 0) {
                    if (getDatabase().getConfig(player).isString("last-whisper")) {
                        Player target = player.getServer().getPlayer(UUID.fromString(getDatabase().getConfig(player).getString("last-whisper")));
                        if (target != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (String words : args) {
                                stringBuilder.append(words);
                                stringBuilder.append(" ");
                            }
                            Players.send(player, "&7You > " + target.getName() + ": " + stringBuilder.toString().strip());
                            Players.send(target, "&7" + player.getName() + " > You: " + stringBuilder.toString().strip());
                            getDatabase().setString(target, "last-whisper", player.getUniqueId().toString());
                            for (Player players : sender.getServer().getOnlinePlayers()) {
                                if (players.hasPermission("players.notify.whispers")) {
                                    Players.send(players, "&7" + player.getName() + " > " + target.getName() + ": " + stringBuilder.toString().strip());
                                }
                            }
                        }
                    }
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