package net.achymake.players.files;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Motd {
    private File file;
    public Motd(File dataFolder) {
        this.file = new File(dataFolder, "motd.yml");
    }
    public boolean exist() {
        return file.exists();
    }
    public FileConfiguration configuration() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public void reload() {
        if (exist()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
                config.save(file);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
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
                throw new RuntimeException(e);
            }
        }
    }
    public boolean motdExist(String motd) {
        return configuration().isList(motd);
    }
    public List<String> getMotds() {
        return new ArrayList<>(configuration().getKeys(false));
    }
    public void sendMotd(CommandSender sender, String motd) {
        if (motdExist(motd)) {
            for (String message : configuration().getStringList(motd)) {
                sender.sendMessage(addColor(message.replaceAll("%player%", sender.getName())));
            }
        } else {
            send(sender, motd + "&c does not exist");
        }
    }
    private void send(CommandSender sender, String message) {
        sender.sendMessage(addColor(message));
    }
    private String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}