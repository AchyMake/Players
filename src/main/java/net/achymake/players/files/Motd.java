package net.achymake.players.files;

import net.achymake.players.Players;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Motd {
    private final File file;
    public Motd(File dataFolder) {
        this.file = new File(dataFolder, "motd.yml");
    }
    public boolean exist() {
        return file.exists();
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public boolean motdExist(String motd) {
        return getConfig().isList(motd);
    }
    public List<String> getMotds() {
        return new ArrayList<>(getConfig().getKeys(false));
    }
    public void sendMotd(CommandSender sender, String motd) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (motdExist(motd)) {
                for (String message : getConfig().getStringList(motd)) {
                    Players.send(player, message.replaceAll("%player%", player.getName()));
                }
            } else {
                Players.send(player, motd + "&c does not exist");
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender commandSender = (ConsoleCommandSender) sender;
            if (motdExist(motd)) {
                for (String message : getConfig().getStringList(motd)) {
                    Players.send(commandSender, message.replaceAll("%player%", commandSender.getName()));
                }
            } else {
                Players.send(commandSender, motd + "&c does not exist");
            }
        }
    }
    public void reload() {
        if (exist()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                Players.sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> motd = new ArrayList<>();
            motd.add("&6Welcome back&f %player%");
            motd.add("&6We missed you!");
            config.addDefault("message-of-the-day", motd);
            List<String> welcome = new ArrayList<>();
            config.addDefault("welcome", welcome);
            welcome.add("&6Welcome&f %player%&6 to the server!");
            List<String> rules = new ArrayList<>();
            rules.add("&6Rules:");
            rules.add("&61.&f No server crashing");
            rules.add("&62.&f No griefing");
            rules.add("&63.&f No Monkieng around! uhh?");
            config.addDefault("rules", rules);
            List<String> help = new ArrayList<>();
            help.add("&6Help:");
            help.add("- https://achy.tebex.io/help");
            config.addDefault("help", help);
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                Players.sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
}