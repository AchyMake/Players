package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InformationCommand implements CommandExecutor, TabCompleter {
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
    public InformationCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (getUserdata().exist(offlinePlayer)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    getMessage().send(player, "&6Information:&f " + offlinePlayer.getName());
                    getMessage().send(player, "&6last online:&f " + simpleDateFormat.format(offlinePlayer.getLastPlayed()));
                    getMessage().send(player, "&6homes:&f " + getUserdata().getHomes(offlinePlayer).size());
                    getMessage().send(player, "&6muted:&f " + getUserdata().isMuted(offlinePlayer));
                    getMessage().send(player, "&6frozen:&f " + getUserdata().isFrozen(offlinePlayer));
                    getMessage().send(player, "&6jailed:&f " + getUserdata().isJailed(offlinePlayer));
                    getMessage().send(player, "&6pvp:&f " + getUserdata().isPVP(offlinePlayer));
                    getMessage().send(player, "&6banned:&f " + getUserdata().isBanned(offlinePlayer));
                    getMessage().send(player, "&6ban-reason:&f " + getUserdata().getBanReason(offlinePlayer));
                    getMessage().send(player, "&6vanished:&f " + getUserdata().isVanished(offlinePlayer));
                } else {
                    getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
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
                for (OfflinePlayer offlinePlayer : getServer().getOfflinePlayers()) {
                    commands.add(offlinePlayer.getName());
                }
            }
        }
        return commands;
    }
}