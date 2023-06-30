package net.achymake.players.version;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

public class UpdateChecker {
    private final Players plugin;
    private final int resourceId;
    public UpdateChecker(Players plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }
    private FileConfiguration getConfig() {
        return Players.getConfiguration();
    }
    private Message getMessage() {
        return Players.getMessage();
    }
    public void getVersion(Consumer<String> consumer) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)).openStream();
                Scanner scanner = new Scanner(inputStream);
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                    scanner.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        });
    }
    public void getUpdate() {
        if (getConfig().getBoolean("notify-update.enable")) {
            (new UpdateChecker(plugin, resourceId)).getVersion((latest) -> {
                getMessage().sendLog(Level.INFO, "checking latest release");
                if (plugin.getDescription().getVersion().equals(latest)) {
                    getMessage().sendLog(Level.INFO, "You are using the latest version");
                } else {
                    getMessage().sendLog(Level.INFO, "New Update: " + latest);
                    getMessage().sendLog(Level.INFO, "Current Version: " + plugin.getDescription().getVersion());
                }
            });
        }
    }
    public void sendMessage(Player player) {
        if (getConfig().getBoolean("notify-update.enable")) {
            (new UpdateChecker(plugin, resourceId)).getVersion((latest) -> {
                if (!plugin.getDescription().getVersion().equals(latest)) {
                    getMessage().send(player,"&6" + plugin.getName() + " Update:&f " + latest);
                    getMessage().send(player,"&6Current Version: &f" + plugin.getDescription().getVersion());
                }
            });
        }
    }
}
