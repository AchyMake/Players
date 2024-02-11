package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RTPCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public RTPCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().hasCooldown(player, "rtp")) {
                    getMessage().sendActionBar(player, "&cYou have to wait&f " + getUserdata().getCooldown(player, "rtp") + "&c seconds");
                } else {
                    getUserdata().addCooldown(player, "rtp");
                    getMessage().sendActionBar(player, "&6Finding safe locations...");
                    randomTeleport(player);
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("force")) {
                    if (player.hasPermission("players.command.rtp.force")) {
                        getMessage().sendActionBar(player, "&6Finding safe locations...");
                        randomTeleport(player);
                    }
                }
            }
        }
        return true;
    }
    public void randomTeleport(Player player) {
        Block block = getUserdata().highestRandomBlock();
        if (block.isLiquid()) {
            getMessage().sendActionBar(player, "&cFinding new location due to liquid block");
            randomTeleport(player);
        } else {
            block.getChunk().load();
            getMessage().sendActionBar(player, "&6Teleporting");
            player.teleport(block.getLocation().add(0.5,1,0.5));
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.rtp.force")) {
                    commands.add("force");
                }
            }
        }
        return commands;
    }
}
