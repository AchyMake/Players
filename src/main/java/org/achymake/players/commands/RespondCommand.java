package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RespondCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public RespondCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!getUserdata().isMuted(player)) {
                if (args.length > 0) {
                    if (getUserdata().getConfig(player).isString("last-whisper")) {
                        String uuidString = getUserdata().getConfig(player).getString("last-whisper");
                        UUID uuid = UUID.fromString(uuidString);
                        Player target = player.getServer().getPlayer(uuid);
                        if (target != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (String words : args) {
                                stringBuilder.append(words);
                                stringBuilder.append(" ");
                            }
                            String builder = stringBuilder.toString().strip();
                            getMessage().send(player, "&7You > " + target.getName() + ": " + builder);
                            getMessage().send(target, "&7" + player.getName() + " > You: " + builder);
                            getUserdata().setString(target, "last-whisper", player.getUniqueId().toString());
                            for (Player players : getServer().getOnlinePlayers()) {
                                if (players.hasPermission("players.notify.whispers")) {
                                    getMessage().send(players, "&7" + player.getName() + " > " + target.getName() + ": " + builder);
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
