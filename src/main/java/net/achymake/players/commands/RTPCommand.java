package net.achymake.players.commands;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.achymake.players.files.Message;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RTPCommand implements CommandExecutor, TabCompleter {
    private FileConfiguration getConfig() {
        return Players.getInstance().getConfig();
    }
    private Database getDatabase() {
        return Players.getDatabase();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (getDatabase().getCommandCooldown().containsKey("rtp-" + player.getUniqueId())) {
                    Long timeElapsed = System.currentTimeMillis() - getDatabase().getCommandCooldown().get("rtp-" + player.getUniqueId());
                    String cooldownTimer = getConfig().getString("commands.cooldown.rtp");
                    Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
                    if (timeElapsed > integer) {
                        getDatabase().getCommandCooldown().put("rtp-" + player.getUniqueId(), System.currentTimeMillis());
                        getMessage().sendActionBar(player, "&6Finding safe locations...");
                        randomTeleport(player);
                    } else {
                        long timer = (integer-timeElapsed);
                        getMessage().send(sender, "&cYou have to wait&f " + String.valueOf(timer).substring(0, String.valueOf(timer).length() - 3) + "&c seconds");
                    }
                } else {
                    getDatabase().getCommandCooldown().put("rtp-" + player.getUniqueId(), System.currentTimeMillis());
                    getMessage().sendActionBar(player, "&6Finding safe locations...");
                    randomTeleport(player);
                }
            }
        }
        return true;
    }
    public void randomTeleport(Player player) {
        Block block = getDatabase().highestRandomBlock();
        if (block.isLiquid()) {
            getMessage().sendActionBar(player, "&cFinding new location due to liquid block");
            randomTeleport(player);
        } else {
            block.getChunk().load();
            getMessage().sendActionBar(player, "&6Teleporting");
            player.teleport(block.getLocation().add(0.5, 1.0, 0.5));
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        return commands;
    }
}